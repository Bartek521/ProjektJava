package com.projekt2115.app.repositories;

import com.projekt2115.app.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByUserID(Long userId);
    List<Ticket> findByEventID(Long eventId);
}
