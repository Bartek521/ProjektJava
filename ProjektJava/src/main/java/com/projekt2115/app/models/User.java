package com.projekt2115.app.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails{
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Artist artist;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + this.statusUser.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
