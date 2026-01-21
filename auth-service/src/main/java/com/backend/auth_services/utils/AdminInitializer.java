package com.backend.auth_services.utils;

import com.backend.auth_services.model.AuthUser;
import com.backend.auth_services.model.Role;
import com.backend.auth_services.model.Status;
import com.backend.auth_services.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final AuthUserRepository authUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${admin.phone}")
    private String adminPhone;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {

        String encoded = passwordEncoder.encode(adminPassword);

        if (!authUserRepository.existsByPhoneNumber(adminPhone)) {

            AuthUser admin = AuthUser.builder()
                    .phoneNumber(adminPhone)
                    .role(Role.ADMIN)
                    .status(Status.ACTIVE)
                    .password(encoded)
                    .build();

            authUserRepository.save(admin);
        }
    }
}
