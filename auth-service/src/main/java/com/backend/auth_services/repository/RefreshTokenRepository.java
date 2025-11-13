package com.backend.auth_services.repository;

import com.backend.auth_services.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByAuthUserId(Long id);
    void deleteByAuthUserId(Long id);
}
