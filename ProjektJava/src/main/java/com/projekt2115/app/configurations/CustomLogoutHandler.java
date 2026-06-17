package com.projekt2115.app.configurations;

import com.projekt2115.app.models.User;
import com.projekt2115.app.models.Vote;
import com.projekt2115.app.models.Poll;
import com.projekt2115.app.repositories.VoteRepository;
import com.projekt2115.app.repositories.PollRepository;
import com.projekt2115.app.repositories.UserRepository;
import com.projekt2115.app.services.PollSessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CustomLogoutHandler implements LogoutSuccessHandler {

    private final PollSessionService pollSessionService;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final PollRepository pollRepository;

    public CustomLogoutHandler(PollSessionService pollSessionService,
                               VoteRepository voteRepository,
                               UserRepository userRepository,
                               PollRepository pollRepository) {
        this.pollSessionService = pollSessionService;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.pollRepository = pollRepository;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null) {
                List<String> pendingVotes = pollSessionService.getPendingVotes();
                for (String rawVote : pendingVotes) {
                    String[] parts = rawVote.split(":");
                    if (parts.length == 2) {
                        Long pollId = Long.parseLong(parts[0]);
                        String selectedOption = parts[1];

                        Poll poll = pollRepository.findById(pollId).orElse(null);

                        if (poll != null) {
                            if (!voteRepository.existsByUserIdAndPollId(user.getId(), poll.getId())) {
                                Vote vote = new Vote();
                                vote.setUser(user);
                                vote.setPoll(poll);
                                vote.setSelectedOption(selectedOption);

                                voteRepository.save(vote);
                            }
                        }
                    }
                }
            }
        }
        pollSessionService.clear();
        response.sendRedirect("/");
    }
}