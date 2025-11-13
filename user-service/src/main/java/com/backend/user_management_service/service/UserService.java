package com.backend.user_management_service.service;

import com.backend.user_management_service.model.User;
import com.backend.user_management_service.repository.UserRepository;
import com.backend.user_management_service.utils.Mapper.UserMapper;
import com.backend.user_management_service.utils.dtos.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long createUser(CreateUserRequest request) {

            // check existing by phone
            Optional<User> existing = userRepository.findByPhoneNumber(request.getPhoneNumber());
            if (existing.isPresent()) {
                return existing.get().getId();
            }

            User saved = userRepository.save(UserMapper.fromCreateUserRequest(request));
            return saved.getId();
        }
}
