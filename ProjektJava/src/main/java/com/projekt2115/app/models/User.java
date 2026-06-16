package com.projekt2115.app.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private String firstName;

    @NotBlank(message = "Nazwisko nie może być puste")
    @Size(min=2, max=80, message = "Nazwisko musi mieć od 2 do 80 liter")
    private String lastName;

    @NotBlank(message = "email jest wymagany")
    @Email(message = "Podaj poprawny adres email(np.nazw@domena.pl)")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Hasło jest wymagane")
    @Size(min = 5, message = "Hasło musi mieć co najmniej 5 znaków")
    private String password;

    @NotNull(message = "Data urodzenia jest wymagana")
    @Past(message = "Data urodzenia musi być z przeszłości")
    private LocalDate birthDate;

    @NotNull(message = "Status użytkownik jest wymagany")
    @Enumerated(EnumType.STRING)
    private UserStatus statusUser=UserStatus.BAMBIK;

    @NotNull(message = "Nazwa użytkownika jest wymagana")
    @NotBlank(message = "Nazwa użytkownika nie może być spacją")
    @Column(unique = true)
    private String nickname;

    @NotNull
    private Integer points = 0;

    @NotNull
    @PastOrPresent(message = "Data nie może być z przyszłości")
    private LocalDate enterDate = LocalDate.now();
}
