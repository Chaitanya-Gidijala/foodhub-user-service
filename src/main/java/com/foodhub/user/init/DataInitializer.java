package com.foodhub.user.init;

import com.foodhub.user.entity.Role;
import com.foodhub.user.entity.User;
import com.foodhub.user.repository.UserRepository;
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
        if (userRepository.findByEmail("admin@foodhub.com").isEmpty()) {
            User admin = new User();
            admin.setUsername("Admin");
            admin.setEmail("admin@foodhub.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Admin user created: admin@foodhub.com / admin123");
        }
    }
}
