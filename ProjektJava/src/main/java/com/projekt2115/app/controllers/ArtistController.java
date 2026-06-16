package com.projekt2115.app.controllers;

import com.projekt2115.app.models.Artist;
import com.projekt2115.app.models.User;
import com.projekt2115.app.models.UserStatus;
import com.projekt2115.app.repositories.ArtistRepository;
import com.projekt2115.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/artists")
public class ArtistController {

    private final ArtistRepository artistRepository;
    private final UserService userService;

    @Autowired
    public ArtistController(ArtistRepository artistRepository, UserService userService) {
        this.artistRepository = artistRepository;
        this.userService = userService;
    }

    @GetMapping
    public String listArtists(Model model) {
        model.addAttribute("artists", artistRepository.findAll());
        model.addAttribute("users", userService.getAllUsers());
        return "artistList";
    }

    @PostMapping("/add")
    public String addArtist(@RequestParam("id_artist") Long idArtist,
                            @RequestParam("groupName") String groupName) {

        // 1. Pobieramy użytkownika z bazy Oracle
        User user = userService.getUserById(idArtist)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika o ID: " + idArtist));

        // 2. Zmiana statusu w tabeli USERS (To już działa)
        user.setStatusUser(UserStatus.ARTIST);
        userService.saveUser(user);

        // 3. Tworzenie profilu w tabeli ARTISTS
        Artist artist = new Artist();
        artist.setGroupName(groupName);
        artist.setUser(user); // Hibernate sam wyciągnie stąd ID i zapisze je w kolumnie user_Id

        // 4. GENEROWANIE PINU (Twój model wymaga pola 'pin' o długości dokładnie 9 znaków!)
        // Generujemy losowy 9-cyfrowy ciąg, np. "123456789"
        String randomPin = String.valueOf((int)((Math.random() * 900000000) + 100000000));
        artist.setPin(randomPin);

        // UWAGA: Nie ustawiamy ręcznie artist.setId_artist(...),
        // ponieważ masz tam adnotację @GeneratedValue i baza Oracle sama nada to ID!

        artistRepository.save(artist);

        return "redirect:/artists";
    }
}