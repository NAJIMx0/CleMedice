package com.cabinet.config;

import com.cabinet.model.Role;
import com.cabinet.model.User;
import com.cabinet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@clemedice.com")) {
            User admin = User.builder()
                    .nom("Administrateur")
                    .email("admin@clemedice.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.MEDECIN_PRINCIPAL)
                    .enabled(true)
                    .build();
            userRepository.save(admin);
            System.out.println("Default admin created: admin@clemedice.com / admin123");
        }
    }
}
