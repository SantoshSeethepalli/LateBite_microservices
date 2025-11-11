package com.backend.user_management_service.service;

import com.backend.user_management_service.model.User;
import com.backend.user_management_service.repository.UserRepository;
import com.backend.user_management_service.utils.Mapper.UserMapper;
import com.backend.user_management_service.utils.dtos.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long createUser(CreateUserRequest request) {

        User newUser = userRepository.save(UserMapper.fromCreateUserRequest(request));

        return newUser.getId();
    }
}
