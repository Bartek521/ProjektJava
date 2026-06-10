package com.projekt2115.app.repositories; // Jeśli Twój folder ma literówkę, zmień końcówkę na .repositowy;

import com.projekt2115.app.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}