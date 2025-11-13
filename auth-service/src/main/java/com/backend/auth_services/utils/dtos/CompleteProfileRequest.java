package com.backend.auth_services.utils.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompleteProfileRequest {

    @NotNull
    private Long authUserId;

    @NotNull
    private ProfileType type;

    @NotNull
    private Object payload;
}

