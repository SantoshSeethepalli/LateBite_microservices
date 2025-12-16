package com.backend.user_management_service.controller;

import com.backend.user_management_service.model.User;
import com.backend.user_management_service.service.UserService;
import com.backend.user_management_service.utils.dtos.CreateUserRequest;
import com.backend.user_management_service.utils.dtos.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Long> createUser(@RequestBody CreateUserRequest request) {

        Long referenceId = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(referenceId);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(
            @RequestHeader("X-Ref-Id") Long userId,
            @RequestHeader("X-Role") String role) {

        if (!role.equals("USER")) {
            return ResponseEntity.status(403).build();
        }

        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/profile")
    public ResponseEntity<String> updateUserProfile(
            @RequestHeader("X-Ref-Id") Long userId,
            @RequestHeader("X-Role") String role,
            @RequestBody UpdateUserRequest request) {

        if (!role.equals("USER")) {
            return ResponseEntity.status(403).build();
        }

        userService.updateUserProfile(userId, request);
        return ResponseEntity.ok("Profile updated successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader("X-Role") String role) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(
            @PathVariable Long userId,
            @RequestHeader("X-Role") String role) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(403).build();
        }

        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
}
