package com.projekt2115.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator (name ="user_seq", sequenceName = "USER_SEQ",allocationSize = 1)
    private Long id;
    @NotBlank(message = "Imię nie może być puste")
    @Size(min=2, max=80, message = "Imię musi mieć od 2 do 80 liter")
    private String FirstName;
    @NotBlank(message = "Nazwisko nie może być puste")
    @Size(min=2, max=80, message = "Nazwisko musi mieć od 2 do 80 liter")
    private String LastName;
    @NotBlank(message = "email jest wymagany")
    @Email(message = "Podaj poprawny adres email(np.nazw@domena.pl)")
    @Column(unique = true)
    private String emial;
    @NotBlank(message = "Hasło jest wymagane")
    private String password;
    @NotNull(message = "Role użytkownik jest wymagany")
    @Enumerated(EnumType.STRING)
    private Role role;


    private String nickname;
    private String bandname;
}
