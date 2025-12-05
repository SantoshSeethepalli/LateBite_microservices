package com.backend.user_management_service.controller;

import com.backend.user_management_service.service.UserService;
import com.backend.user_management_service.utils.dtos.CreateUserRequest;
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
}
