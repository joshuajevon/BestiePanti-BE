package com.app.bestiepanti.service;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.bestiepanti.dto.request.UserRequest;
import com.app.bestiepanti.dto.response.UserResponse;
import com.app.bestiepanti.model.Donatur;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonaturRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DonaturRepository donaturRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserRequest userRequest) {
        UserApp user = new UserApp();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setRole(UserApp.ROLE_DONATUR);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userRepository.save(user);

        Donatur donatur = saveToDonatur(userRequest, user);
        return createUserResponse(user, donatur);
    }

    private Donatur saveToDonatur(UserRequest userRequest, UserApp user) {
        Donatur donatur = new Donatur();
        donatur.setUser(user);
        donatur.setAddress(userRequest.getAddress());
        donatur.setGender(userRequest.getGender());
        donatur.setPhone(userRequest.getPhone());
        LocalDate dob = LocalDate.parse(userRequest.getDob()); 
        donatur.setDob(dob);
        donaturRepository.save(donatur);
        return donatur;
    }

    private UserResponse createUserResponse(UserApp userApp, Donatur donatur) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userApp.getId());
        userResponse.setName(userApp.getName());
        userResponse.setEmail(userApp.getEmail());
        userResponse.setRole(userApp.getRole());
        userResponse.setPhone(donatur.getPhone());
        userResponse.setDob(donatur.getPhone());
        userResponse.setGender(donatur.getGender());
        userResponse.setAddress(donatur.getAddress());
        return userResponse;
    }
}

