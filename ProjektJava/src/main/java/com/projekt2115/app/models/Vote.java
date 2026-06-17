package com.projekt2115.app.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "VOTES", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "poll_id"})
})
@Data
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @Column(nullable = false)
    private String selectedOption;
}
