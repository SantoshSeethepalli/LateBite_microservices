package com.backend.user_management_service.repository;

import com.backend.user_management_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
