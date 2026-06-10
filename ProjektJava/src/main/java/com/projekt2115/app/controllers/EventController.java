package com.projekt2115.app.controllers;

import com.projekt2115.app.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }
    @GetMapping("/events")
    public String listEvents(Model model) {
        model.addAttribute("events",eventService.getAllEvents());
        return "events-list";
    }
}
