package com.olivier.devhub.user.config;

import com.olivier.devhub.user.domain.UserAccount;
import com.olivier.devhub.user.domain.UserRole;
import com.olivier.devhub.user.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Locale;
import java.util.UUID;

@Configuration
public class AdminBootstrap {

    @Bean
    ApplicationRunner createAdminIfConfigured(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.email:}") String email,
            @Value("${app.admin.password:}") String password
    ) {
        return arguments -> {
            if (email.isBlank() && password.isBlank()) {
                return;
            }
            if (email.isBlank() || password.isBlank()) {
                throw new IllegalStateException("ADMIN_EMAIL and ADMIN_PASSWORD must be configured together.");
            }

            String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
            if (userAccountRepository.findByEmail(normalizedEmail).isEmpty()) {
                userAccountRepository.save(new UserAccount(
                        UUID.randomUUID(),
                        normalizedEmail,
                        passwordEncoder.encode(password),
                        UserRole.ADMIN
                ));
            }
        };
    }
}
