package com.agafonov.Test.service;

import com.agafonov.Test.dto.UserRequest;
import com.agafonov.Test.dto.UserResponse;
import com.agafonov.Test.exception.ResourceNotFoundException;
import com.agafonov.Test.model.Role;
import com.agafonov.Test.model.User;
import com.agafonov.Test.repository.RoleRepository;
import com.agafonov.Test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "users")
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @CacheEvict(allEntries = true)
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }
        Role role = roleRepository.findByRoleName(userRequest.getRoleName())
                .orElseGet(() -> roleRepository.save(
                        Role.builder().roleName(userRequest.getRoleName()).build()));

        User user = modelMapper.map(userRequest, User.class);
        user.setRole(role);

        User savedUser = userRepository.save(user);
        log.info("Created new user with ID: {}", savedUser.getId());

        return modelMapper.map(savedUser, UserResponse.class);
    }

    @Cacheable(key = "#userID")
    public UserResponse getUser(UUID userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userID));
        return modelMapper.map(user, UserResponse.class);
    }

    @CachePut(key = "#userID")
    public UserResponse updateUser(UUID userID, UserRequest userRequest) {
        User existingUser = userRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userID));

        if (!existingUser.getPhoneNumber().equals(userRequest.getPhoneNumber())
                && userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("Phone number already exists");
        }

        Role role = roleRepository.findByRoleName(userRequest.getRoleName())
                .orElseGet(() -> roleRepository.save(
                        Role.builder().roleName(userRequest.getRoleName()).build()));

        modelMapper.map(userRequest, existingUser);
        existingUser.setRole(role);

        User updatedUser = userRepository.save(existingUser);
        log.info("Updated user with ID: {}", userID);

        return modelMapper.map(updatedUser, UserResponse.class);
    }

    @CacheEvict(key = "#userID")
    public void deleteUser(UUID userID) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userID));

        userRepository.delete(user);
        log.info("Deleted user with ID: {}", userID);
    }
}
