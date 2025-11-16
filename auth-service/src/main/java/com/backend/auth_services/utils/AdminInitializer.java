package com.backend.auth_services.utils;

import com.backend.auth_services.model.AuthUser;
import com.backend.auth_services.model.Role;
import com.backend.auth_services.model.Status;
import com.backend.auth_services.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final AuthUserRepository authUserRepository;

    @Override
    public void run(String... args) {

        String adminPhone = "9988776655";

        if (!authUserRepository.existsByPhoneNumber(adminPhone)) {

            AuthUser admin = AuthUser.builder()
                    .phoneNumber(adminPhone)
                    .role(Role.ADMIN)
                    .status(Status.ACTIVE)
                    .build();

            authUserRepository.save(admin);
        }
    }
}
