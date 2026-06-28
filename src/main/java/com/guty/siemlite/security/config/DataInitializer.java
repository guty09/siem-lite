package com.guty.siemlite.security.config;

import com.guty.siemlite.security.model.Role;
import com.guty.siemlite.security.model.User;
import com.guty.siemlite.security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedDefaultUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            createUserIfMissing(userRepository, passwordEncoder, "admin", "admin123", Role.ADMIN);
            createUserIfMissing(userRepository, passwordEncoder, "analyst", "analyst123", Role.ANALYST);
            createUserIfMissing(userRepository, passwordEncoder, "viewer", "viewer123", Role.VIEWER);
        };
    }

    private void createUserIfMissing(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String username,
            String rawPassword,
            Role role
    ) {
        if (!userRepository.existsByUsername(username)) {
            User user = new User(username, passwordEncoder.encode(rawPassword), role);
            userRepository.save(user);
        }
    }
}
