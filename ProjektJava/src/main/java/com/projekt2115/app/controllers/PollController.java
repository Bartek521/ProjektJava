package com.projekt2115.app.controllers;

import com.projekt2115.app.models.Poll;
import com.projekt2115.app.models.User;
import com.projekt2115.app.repositories.PollRepository;
import com.projekt2115.app.repositories.UserRepository;
import com.projekt2115.app.repositories.VoteRepository;
import com.projekt2115.app.services.DictionaryClientService;
import com.projekt2115.app.services.PollSessionService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping("/polls")
public class PollController {
    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final PollSessionService pollSessionService;
    private final DictionaryClientService dictionaryClientService;

    public PollController(PollRepository pollRepository,
                          VoteRepository voteRepository,
                          UserRepository userRepository,
                          PollSessionService pollSessionService,
                          DictionaryClientService dictionaryClientService) {
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.pollSessionService = pollSessionService;
        this.dictionaryClientService = dictionaryClientService;
    }

    @GetMapping
    public String listPolls(Model model, Principal principal) {
        List<Poll> allPolls = pollRepository.findAll();
        model.addAttribute("polls", allPolls);

        List<Long> votedPollIdsInSession = new java.util.ArrayList<>();

        if (pollSessionService != null && pollSessionService.getPendingVotes() != null) {
            try {
                votedPollIdsInSession = pollSessionService.getPendingVotes().stream()
                        .filter(v -> v != null && v.contains(":")) // Chroni przed błędnymi wpisami
                        .map(v -> v.split(":")[0])
                        .map(Long::parseLong)
                        .toList();
            } catch (Exception e) {

                votedPollIdsInSession = new java.util.ArrayList<>();
            }
        }

        model.addAttribute("votedInSession", votedPollIdsInSession);

        return "pollList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("poll", new Poll());
        return "pollAdd";
    }
    @PostMapping("/add")
    public String createPoll(@RequestParam("question") String question,
                             @RequestParam("rawOptions") String rawOptions,
                             Authentication authentication,
                             Model model) {
        boolean hasValidContext = false;
        String[] words = question.replaceAll("[^a-zA-Z0-9ąęćłńóśźżĄĘĆŁŃÓŚŹŻ ]","").split("\\s+");

        for (String word:words){
            if(dictionaryClientService.isWordInDictionary(word)){
                hasValidContext = true;
                break;
            }
        }
        if(!hasValidContext){
            model.addAttribute("error","Temat ankiet musi być związany z branżą muzyczną, musi zawierać słowo ze słownika");
            return "pollAdd";
        }
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<User> creator = userRepository.findByEmail(authentication.getName());
            if (creator.isPresent()) {
                Poll poll = new Poll();
                poll.setQuestion(question);
                List<String> optionsList = Arrays.stream(rawOptions.split(","))
                        .map(String::trim)
                        .filter(opt -> !opt.isEmpty())
                        .toList();

                poll.setOptions(optionsList);
                poll.setCreator(creator.get());

                pollRepository.save(poll);
            }
        }
        return "redirect:/polls";
    }

    @PostMapping("/vote")
    public String voteInPoll(@RequestParam("pollId") Long pollId,
                             @RequestParam("option") String option,
                             Authentication authentication,
                             Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        Optional<User> currentUser = userRepository.findByEmail(authentication.getName());
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            if (voteRepository.existsByUserIdAndPollId(user.getId(), pollId)) {
                model.addAttribute("error", "Oddałeś już głos w tej ankiecie w przeszłości!");
                model.addAttribute("polls", pollRepository.findAll());
                model.addAttribute("pendingVotes", pollSessionService.getPendingVotes());
                return "pollList";
            }
            boolean alreadyVotedInSession = pollSessionService.getPendingVotes().stream()
                    .anyMatch(v -> v.startsWith(pollId + ":"));
            if (alreadyVotedInSession) {
                model.addAttribute("error", "W tej sesji wybrałeś już opcję dla tej ankiety! Zostanie ona zapisana po wylogowaniu.");
                model.addAttribute("polls", pollRepository.findAll());
                model.addAttribute("pendingVotes", pollSessionService.getPendingVotes());
                return "pollList";
            }

            pollSessionService.addVote(pollId + ":" + option);
        }

        return "redirect:/polls";
    }
}
