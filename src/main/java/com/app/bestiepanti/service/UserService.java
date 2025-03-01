package com.app.bestiepanti.service;

import java.time.LocalDate;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.bestiepanti.configuration.JwtConfig;
import com.app.bestiepanti.dto.request.LoginRequest;
import com.app.bestiepanti.dto.request.RegisterRequest;
import com.app.bestiepanti.dto.response.AdminResponse;
import com.app.bestiepanti.dto.response.donatur.DonaturResponse;
import com.app.bestiepanti.dto.response.panti.PantiResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.Donatur;
import com.app.bestiepanti.model.Panti;
import com.app.bestiepanti.model.Role;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonaturRepository;
import com.app.bestiepanti.repository.PantiRepository;
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
    private final PantiRepository pantiRepository;
    private final JwtConfig jwtConfig;

    public DonaturResponse register(RegisterRequest registerRequest) {
        UserApp user = new UserApp();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());

        Role role = roleRepository.findByName(UserApp.ROLE_DONATUR);
        user.setRole(role);

        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(user);

        Donatur donatur = saveToDonatur(registerRequest, user);

        String jwtToken = jwtService.generateToken(user);
        return createDonaturResponse(user, donatur, jwtToken);
    }

    public Object login(LoginRequest loginRequest) throws UserNotFoundException {
        UserApp user = findUserByEmail(loginRequest.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            throw new UserNotFoundException("Email atau kata sandi tidak valid. Silakan coba lagi.");
        }
        String jwtToken = jwtService.generateToken(user);
        String existingToken = jwtConfig.getActiveToken(loginRequest.getEmail());
        if(existingToken != null) jwtConfig.blacklistToken(existingToken);
        jwtConfig.storeActiveToken(loginRequest.getEmail(), jwtToken);
        
        if(user.getRole().getName().equals(UserApp.ROLE_DONATUR)){
            Donatur donatur = donaturRepository.findByUserId(user.getId());
            return createDonaturResponse(user, donatur, jwtToken);
        } else if(user.getRole().getName().equals(UserApp.ROLE_PANTI)){
            Panti panti = pantiRepository.findByUserId(user.getId());
            return createPantiResponse(user, panti, jwtToken);
        } else if(user.getRole().getName().equals(UserApp.ROLE_ADMIN)){
            return createAdminResponse(user, jwtToken);
        }
        throw new UserNotFoundException("Role tidak ditemukan untuk pengguna. Silakan hubungi dukungan.");
    }

    public UserApp findUserByEmail(String email) throws UserNotFoundException {
        UserApp user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException("Tidak ditemukan akun dengan alamat email ini. Silakan mendaftar."));
        return user;
    }

    public Donatur saveToDonatur(RegisterRequest userRequest, UserApp user) {
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

    public Object getUser() throws UserNotFoundException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserApp user = findUserByEmail(email);

        if(user.getRole().getName().equals(UserApp.ROLE_DONATUR)){
            Donatur donatur = donaturRepository.findByUserId(user.getId());
            return createDonaturResponse(user, donatur, null);
        } else if(user.getRole().getName().equals(UserApp.ROLE_PANTI)){
            Panti panti = pantiRepository.findByUserId(user.getId());
            return createPantiResponse(user, panti, null);
        }
        return createAdminResponse(user, null);
    }

    public AdminResponse createAdminResponse(UserApp userApp, String token) {
        return AdminResponse.builder()
                .id(userApp.getId())
                .name(userApp.getName())
                .email(userApp.getEmail())
                .role(userApp.getRole().getName())
                .token(token)
                .build();
    }

    public DonaturResponse createDonaturResponse(UserApp userApp, Donatur donatur, String token) {
        return DonaturResponse.builder()
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

    public PantiResponse createPantiResponse(UserApp userApp, Panti panti, String jwtToken) {
        return PantiResponse.builder()
                .id(userApp.getId())
                .name(userApp.getName())
                .email(userApp.getEmail())
                .role(userApp.getRole().getName())
                .description(panti.getDescription())
                .image(panti.getImage())
                .phone(panti.getPhone())
                .donationTypes(panti.getDonationTypes())
                .isUrgent(panti.getIsUrgent())
                .address(panti.getAddress())
                .qris(panti.getQris())
                .token(jwtToken)
                .build();
    }
}
