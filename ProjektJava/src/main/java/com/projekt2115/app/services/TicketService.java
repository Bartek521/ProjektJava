package com.projekt2115.app.services;

import com.projekt2115.app.models.Ticket;
import com.projekt2115.app.repositories.EventRepository;
import com.projekt2115.app.repositories.TicketRepository;
import com.projekt2115.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository,
                         EventRepository eventRepository,
                         UserRepository userRepository){
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public List<Ticket> getAllTicket(){
        return ticketRepository.findAll();
    }
    public Optional<Ticket> getTicketById(Long id){
        return ticketRepository.findById(id);
    }
    public List<Ticket> getTicketsByUser(Long userid){
        return ticketRepository.findByUserID(userid);
    }
    public List<Ticket> getTicketsByEvent(Long eventid){
        return ticketRepository.findByEventID(eventid);
    }
    public Ticket saveTicket(Ticket ticket){
        if (ticket.getEvent()==null || ticket.getEvent().getId()==null ||!eventRepository.existsById(ticket.getEvent().getId())) {
            throw new IllegalArgumentException("Nie można kupić biletu bo taki koncert nie istenieje!");
        }

        if (ticket.getUser() == null || ticket.getUser().getId() == null ||
                !userRepository.existsById(ticket.getUser().getId())) {
            throw new IllegalArgumentException("Nie można kupić biletu bo użytkownik nie istnieje");
        }

        return ticketRepository.save(ticket);
    }
    public void deleteTicket(Long id){
        ticketRepository.deleteById(id);
    }
}
