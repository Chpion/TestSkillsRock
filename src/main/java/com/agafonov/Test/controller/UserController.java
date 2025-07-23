package com.agafonov.Test.controller;

import com.agafonov.Test.dto.UserRequest;
import com.agafonov.Test.dto.UserResponse;
import com.agafonov.Test.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/createNewUser")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/users")
    public ResponseEntity<UserResponse> getUser(@RequestParam UUID userID) {
        UserResponse user = userService.getUser(userID);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/userDetailsUpdate")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserRequest userRequest,
                                                   @RequestParam UUID userID) {
        UserResponse updatedUser = userService.updateUser(userID, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUser(@RequestParam UUID userID) {
        userService.deleteUser(userID);
        return ResponseEntity.noContent().build();
    }
}
