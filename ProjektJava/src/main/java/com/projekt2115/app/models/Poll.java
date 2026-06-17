package com.projekt2115.app.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table( name = "POLLS")
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    @ElementCollection
    private List<String> options;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private  User creator;
}
