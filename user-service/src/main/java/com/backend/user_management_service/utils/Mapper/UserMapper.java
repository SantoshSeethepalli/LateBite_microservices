package com.backend.user_management_service.utils.Mapper;

import com.backend.user_management_service.model.User;
import com.backend.user_management_service.utils.dtos.CreateUserRequest;

import java.time.LocalDateTime;

public class UserMapper {

    public static User fromCreateUserRequest(CreateUserRequest request) {

        return User.builder()
                .username(request.getUsername())
                .phoneNumber(request.getPhoneNumber())
                .profilePhoto(request.getProfilePhoto())
                .updatedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
