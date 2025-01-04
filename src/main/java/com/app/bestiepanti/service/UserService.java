package com.app.bestiepanti.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.bestiepanti.dto.request.UserRequest;
import com.app.bestiepanti.dto.response.UserResponse;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserRequest userRequest) {
        UserApp user = new UserApp();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setRole(UserApp.ROLE_DONATUR);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword())); // Encode password
        userRepository.save(user);

        return createUserResponse(user);
    }

    private UserResponse createUserResponse(UserApp userApp) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userApp.getId());
        userResponse.setName(userApp.getName());
        userResponse.setEmail(userApp.getEmail());
        userResponse.setRole(userApp.getRole());
        return userResponse;
    }
}

