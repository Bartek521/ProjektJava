package com.projekt2115.app.controllers;

import com.projekt2115.app.models.Event;
import com.projekt2115.app.services.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.naming.Binding;

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
        model.addAttribute("newEvent", new Event());
        return "EventList";
    }
    @PostMapping("/events/add")
    public String addEvent(@Valid @ModelAttribute("newEvent") Event event, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("events", eventService.getAllEvents());
            return "eventList";
        }
        eventService.saveEvent(event);
        return "redirect:/events";
    }
    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable("id") Long id) {
        eventService.deleteEvent(id);
        return "redirect:/events";
    }
    @GetMapping("/events/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Event event = eventService.getEventById(id);
        if (event != null) {
            model.addAttribute("eventToEdit", event);
            return "eventEdit";
        }
        return "redirect:/events";
    }

    @PostMapping("/events/update")
    public String updateEvent(@ModelAttribute("eventToEdit") Event event) {
        eventService.saveEvent(event);
        return "redirect:/events";
    }
}
