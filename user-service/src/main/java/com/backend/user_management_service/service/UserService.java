package com.backend.user_management_service.service;

import com.backend.user_management_service.model.User;
import com.backend.user_management_service.repository.UserRepository;
import com.backend.user_management_service.utils.Mapper.UserMapper;
import com.backend.user_management_service.utils.dtos.CreateUserRequest;
import com.backend.user_management_service.utils.dtos.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public User getUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found with ID: " + userId)
                );
    }

    @Transactional
    public void updateUserProfile(Long userId, UpdateUserRequest request) {
        User user = getUserById(userId);

        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }

        if (request.getProfilePhoto() != null && !request.getProfilePhoto().isEmpty()) {
            user.setProfilePhoto(request.getProfilePhoto());
        }

        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
