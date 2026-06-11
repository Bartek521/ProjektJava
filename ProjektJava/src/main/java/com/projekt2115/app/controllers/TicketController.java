package com.projekt2115.app.controllers;

import com.projekt2115.app.models.Ticket;
import com.projekt2115.app.models.TicketType;
import com.projekt2115.app.services.EventService;
import com.projekt2115.app.services.TicketService;
import com.projekt2115.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public TicketController( TicketService ticketService, EventService eventService, UserService userService){
        this.ticketService = ticketService;
        this.userService = userService;
        this.eventService = eventService;
    }
    @GetMapping("/book")
    public String showBookingForm(Model model){
        model.addAttribute("ticket",new Ticket());
        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("ticketTypes", TicketType.values());
        return "bookTicket";
    }

    @PostMapping("/book")
    public String bookTicket(@ModelAttribute("ticket") Ticket ticket, Model model){
        try{
            ticketService.saveTicket(ticket);
            return "redirect:/tickets/myTickets?userId="+ticket.getUser().getId();
        }catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("events", eventService.getAllEvents());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("ticketTypes", TicketType.values());
            return "bookTicket";
        }
    }

    @GetMapping("/myTickets")
    public String showUserTickets(@RequestParam("userId") Long userId, Model model){
        model.addAttribute("tickets",ticketService.getTicketsByUser(userId));
        userService.getUserById(userId).ifPresent(user -> model.addAttribute("user",user));
        return "userTickets";
    }
}