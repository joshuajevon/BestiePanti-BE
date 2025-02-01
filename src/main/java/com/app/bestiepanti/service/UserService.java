package com.app.bestiepanti.service;

import java.time.LocalDate;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.bestiepanti.dto.request.LoginRequest;
import com.app.bestiepanti.dto.request.RegisterRequest;
import com.app.bestiepanti.dto.response.UserResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.Donatur;
import com.app.bestiepanti.model.Role;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonaturRepository;
import com.app.bestiepanti.repository.RoleRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final DonaturRepository donaturRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserResponse register(RegisterRequest registerRequest) {
        UserApp user = new UserApp();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());

        Role role = roleRepository.findByName(UserApp.ROLE_DONATUR);
        user.setRole(role);

        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);

        Donatur donatur = saveToDonatur(registerRequest, user);

        String jwtToken = jwtService.generateToken(user);
        return createUserResponse(user, donatur, jwtToken);
    }

    public UserResponse login(LoginRequest loginRequest) throws UserNotFoundException {
        UserApp user = findUserByEmail(loginRequest.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("Invalid email or password. Please try again.");
        }
        Donatur donatur = donaturRepository.findByUserId(user.getId());
        String jwtToken = jwtService.generateToken(user);
        return createUserResponse(user, donatur, jwtToken);
    }

    public UserApp findUserByEmail(String email) throws UserNotFoundException {
        UserApp user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException("No account found with this email address. Please sign up."));
        return user;
    }

    private Donatur saveToDonatur(RegisterRequest userRequest, UserApp user) {
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

    private UserResponse createUserResponse(UserApp userApp, Donatur donatur, String token) {
        return UserResponse.builder()
                .id(userApp.getId())
                .name(userApp.getName())
                .email(userApp.getEmail())
                .role(userApp.getRole().getName())
                .phone(donatur.getPhone())
                .dob(donatur.getDob().toString())
                .gender(donatur.getGender())
                .address(donatur.getAddress())
                .token(token)
                .build();
    }
}
