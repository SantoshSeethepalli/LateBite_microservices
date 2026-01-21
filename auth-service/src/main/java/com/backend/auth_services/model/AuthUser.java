package com.backend.auth_services.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "t_auth_user",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"phoneNumber", "role"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long referenceId;

    // For admin password-based authentication only
    private String password;
}
