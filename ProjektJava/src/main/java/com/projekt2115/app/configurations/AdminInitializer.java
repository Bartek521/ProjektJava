package com.projekt2115.app.configurations;

import com.projekt2115.app.models.User;
import com.projekt2115.app.models.UserStatus; // Import Twojego enuma statusu
import com.projekt2115.app.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "JLuzak@admin.com";

        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setNickname("Admin2115");
            admin.setFirstName("Jakub");
            admin.setLastName("Luzak");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("Luzak2115"));
            admin.setBirthDate(LocalDate.of(2005, 11, 15));
            admin.setStatusUser(UserStatus.ADMIN);
            userRepository.save(admin);

            System.out.println("======> AdminInitializer: Jakub Luzak (Admin2115) dodany do bazy Oracle! <======");
        } else {
            System.out.println("======> AdminInitializer: Admin o mailu JLuzak@admin.com już istnieje. Pomijam. <======");
        }
    }
}