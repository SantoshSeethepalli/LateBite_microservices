package com.backend.auth_services.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_refresh_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long authUserId;

    @Column(length = 500)
    private String refreshTokenHash;

    private Long expiresAt;
}
