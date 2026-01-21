package com.backend.auth_services.repository;


import com.backend.auth_services.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findFirstByPhoneNumberAndRole(String phone, Role role);
    Optional<AuthUser> findByRole(Role role);
    boolean existsByPhoneNumber(String adminPhone);
}
