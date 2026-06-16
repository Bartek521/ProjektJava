package com.projekt2115.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="Artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_artist;

    @OneToOne
    @JoinColumn(name = "user_Id",referencedColumnName = "id", nullable = false)
    private User user;

    @NotBlank
    @NotNull(message = "Artysta musi należeć do grupy")
    private String groupName;

    @NotNull
    @NotBlank
    @Column(unique = true)
    @Size(min=9,max=9, message = "Artysta musi mieć pin")
    private String pin;
}
