package com.projekt2115.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDateTime;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime eventDate;

    @NotNull(message = "Data zakończenia koncertu musi być podana")
    @Future(message = "Data końca eventu musi być z przyszłości")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "end_event")
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="artist_id", referencedColumnName = "id_artist",nullable = false)
    @NotNull(message = "Artysta który tworzy  event musi być przypisany")
    private Artist artist;

    @NotNull(message = "Ilość wolnych miejsc jest wymagana")
    @Min(value = 0, message = "Ilość wolnych miejsc nie może być ujemna")
    @Column(name = "available_seats")
    private Integer availableSeats;

    @AssertTrue(message = "Data zakończenia koncertu nie może być wcześniejsza niż data rozpoczęcia")
    public boolean isEndDateValid(){
        if(eventDate == null || endDate == null) return true;
        return endDate.isAfter(eventDate);
    }


}