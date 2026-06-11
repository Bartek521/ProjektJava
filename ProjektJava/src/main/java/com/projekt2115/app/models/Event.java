package com.projekt2115.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@Table(name = "EVENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    @SequenceGenerator(name = "event_seq", sequenceName = "EVENT_SEQ", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Nazwa koncertu nie może być pusta ani składać się z samych spacji")
    @Size(min = 1, max = 100, message = "Nazwa musi mieć od 1 do 100 znaków")
    private String name;

    @NotBlank(message = "Lokalizacja jest wymagana")
    private String location;

    @NotNull(message = "Cena bielu jest wymagana")
    @Min(value = 0, message="Cena biletu nie może być ujemna")
    private Double ticketPrice;

    @NotNull(message = "Data koncertu musi być podana")
    @Future(message = "Nie można dodać koncertu z przeszłości")
    @Temporal(TemporalType.DATE)
    private Date eventDate;

}