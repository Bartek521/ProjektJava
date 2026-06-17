package com.projekt2115.app.controllers;

import com.projekt2115.app.models.Ticket;
import com.projekt2115.app.models.TicketType;
import com.projekt2115.app.models.User;
import com.projekt2115.app.services.EventService;
import com.projekt2115.app.services.TicketService;
import com.projekt2115.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public TicketController(TicketService ticketService, EventService eventService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping("/book")
    public String showBookingForm(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        Ticket ticket = new Ticket();
        String loggedInEmail = principal.getName();
        User loggedInUser = userService.getUserByEmail(loggedInEmail)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika!"));

        // Sprawdzamy status zalogowanego użytkownika
        String userStatus = loggedInUser.getStatusUser().toString();

        if (!userStatus.equals("ADMIN")) {
            // Bambik lub Artist – przypisujemy usera na sztywno do obiektu biletu
            ticket.setUser(loggedInUser);
            model.addAttribute("isAdmin", false);
        } else {
            // Admin – pozwalamy mu na wybór użytkowników i przekazujemy listę do widoku
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("isAdmin", true);
        }

        model.addAttribute("ticket", ticket);
        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("ticketTypes", TicketType.values());
        return "bookTicket";
    }

    @PostMapping("/book")
    public String bookTicket(@ModelAttribute("ticket") Ticket ticket, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            String loggedInEmail = principal.getName();
            User loggedInUser = userService.getUserByEmail(loggedInEmail)
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika!"));

            // Twarde zabezpieczenie backendowe: Jeśli to NIE admin, nadpisujemy pole user,
            // żeby sprytny użytkownik nie zmienił ID w formularzu przez F12
            if (!loggedInUser.getStatusUser().toString().equals("ADMIN")) {
                ticket.setUser(loggedInUser);
            }

            ticketService.saveTicket(ticket);
            return "redirect:/tickets/myTickets";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("events", eventService.getAllEvents());
            model.addAttribute("ticketTypes", TicketType.values());

            // W razie błędu walidacji musimy ponownie sprawdzić rolę, aby poprawnie wyrenderować formularz
            String loggedInEmail = principal.getName();
            User loggedInUser = userService.getUserByEmail(loggedInEmail).get();
            if (loggedInUser.getStatusUser().toString().equals("ADMIN")) {
                model.addAttribute("users", userService.getAllUsers());
                model.addAttribute("isAdmin", true);
            } else {
                model.addAttribute("isAdmin", false);
            }
            return "bookTicket";
        }
    }

    @GetMapping("/myTickets")
    public String showUserTickets(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String loggedInEmail = principal.getName();

        User user = userService.getUserByEmail(loggedInEmail)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika dla sesji: " + loggedInEmail));

        model.addAttribute("tickets", ticketService.getTicketsByUser(user.getId()));
        model.addAttribute("user", user);

        return "userTickets";
    }
}