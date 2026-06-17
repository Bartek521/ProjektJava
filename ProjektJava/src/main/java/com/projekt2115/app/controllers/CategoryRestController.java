package com.projekt2115.app.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dictionary")
public class CategoryRestController {


    private final List<String> validKeywords = List.of(
            "koncert", "muzyka", "album", "miasto" , "tracklista", "płyta", "bis", "rap", "trap", "2115", "piosenka", "epka", "singiel",
            "feat","usłyszeć","merch", "drop", "bilet" ,"numer", "track", "banger","Bedoesiara","Bedoesiarą","PRP"
    );
    @GetMapping("/verify")
    public boolean verifyWord(@RequestParam("word") String word) {
        if (word == null || word.isBlank()) {
            return false;
        }
        return validKeywords.contains(word.toLowerCase().trim());
    }
}