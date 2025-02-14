package com.app.bestiepanti.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.bestiepanti.configuration.ApplicationConfig;
import com.app.bestiepanti.dto.request.PantiRequest;
import com.app.bestiepanti.dto.response.PantiResponse;
import com.app.bestiepanti.model.Panti;
import com.app.bestiepanti.model.Role;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.PantiRepository;
import com.app.bestiepanti.repository.RoleRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PantiService {
 
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PantiRepository pantiRepository;
    private final JwtService jwtService;
    private final ApplicationConfig applicationConfig;

    public PantiResponse createPanti(PantiRequest request){
        UserApp user = new UserApp();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
 
        Role role = roleRepository.findByName(UserApp.ROLE_PANTI);
        user.setRole(role);
 
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
 
        Panti panti = saveToPanti(request, user);
        String jwtToken = jwtService.generateToken(user);
        return createPantiResponse(user, panti, jwtToken);
    }
 
    public Panti saveToPanti(PantiRequest request, UserApp user){
        Panti panti = new Panti();
        storeImage(request, panti);
        storeQris(request, panti);
        panti.setDescription(request.getDescription());
        panti.setPhone(request.getPhone());
        panti.setDonationTypes(request.getDonationTypes());
        panti.setIsUrgent(Integer.parseInt(request.getIsUrgent()));
        panti.setAddress(request.getAddress());
        panti.setUser(user);
        pantiRepository.save(panti);
        return panti;
    }

    private void storeQris(PantiRequest request, Panti panti) {
        try {
            if (request.getQris() != null && !request.getQris().isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + request.getName() + "_" + request.getQris().getOriginalFilename();
                Path filePath = Paths.get(applicationConfig.getQrisUploadDir(), fileName);
                try {
                    Files.write(filePath, request.getQris().getBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save image", e);
                }
                panti.setQris(fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private void storeImage(PantiRequest request, Panti panti) {
        try {
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                List<String> imagePaths = request.getImage().stream().map(image -> {
                    String fileName = System.currentTimeMillis() + "_" + request.getName() + "_" + image.getOriginalFilename();
                    Path filePath = Paths.get(applicationConfig.getImageUploadDir(), fileName);
                    try {
                        Files.write(filePath, image.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to save image", e);
                    }
                    return fileName;
                }).collect(Collectors.toList());
                panti.setImage(imagePaths);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save images", e);
        }
    }

    public PantiResponse createPantiResponse(UserApp userApp, Panti panti, String jwtToken) {
        return PantiResponse.builder()
                .id(userApp.getId())
                .name(userApp.getName())
                .email(userApp.getEmail())
                .role(userApp.getRole().getName())
                .image(panti.getImage())
                .description(panti.getDescription())
                .phone(panti.getPhone())
                .donationTypes(panti.getDonationTypes())
                .isUrgent(panti.getIsUrgent())
                .address(panti.getAddress())
                .qris(panti.getQris())
                .token(jwtToken)
                .build();
    }
   
}
