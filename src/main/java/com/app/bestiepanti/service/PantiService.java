package com.app.bestiepanti.service;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.bestiepanti.configuration.ApplicationConfig;
import com.app.bestiepanti.dto.request.panti.CreatePantiRequest;
import com.app.bestiepanti.dto.request.panti.ImageRequest;
import com.app.bestiepanti.dto.request.panti.UpdatePantiRequest;
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

    public PantiResponse createPanti(CreatePantiRequest request){
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
 
    public Panti saveToPanti(CreatePantiRequest request, UserApp user){
        Panti panti = new Panti();
        processImage(request, panti);
        processQris(request, panti);
        panti.setDescription(request.getDescription());
        panti.setPhone(request.getPhone());
        panti.setDonationTypes(request.getDonationTypes());
        panti.setIsUrgent(Integer.parseInt(request.getIsUrgent()));
        panti.setAddress(request.getAddress());
        panti.setUser(user);
        pantiRepository.save(panti);
        return panti;
    }

    private void processQris(ImageRequest request, Panti panti) {
        try {
            if (request.getQris() != null && !request.getQris().isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + request.getName() + "_" + request.getQris().getOriginalFilename();
                Path filePath = Paths.get(applicationConfig.getQrisUploadDir(), fileName);
                try {
                    if(panti.getQris() != null){
                        String prevFileName = panti.getQris();
                        Path prevFilePath = Paths.get(applicationConfig.getQrisUploadDir(), prevFileName);
                        if (Files.exists(prevFilePath)) {
                            Files.delete(prevFilePath);
                        }
                    }
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

    private void processImage(ImageRequest request, Panti panti) {
        try {
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                List<String> imagePaths = new ArrayList<>();
                for (MultipartFile image : request.getImage()) {
                    String fileName = System.currentTimeMillis() + "_" + request.getName() + "_" + image.getOriginalFilename();
                    Path filePath = Paths.get(applicationConfig.getImageUploadDir(), fileName);
                    
                    try {
                        if(panti.getImage() != null){
                            List<String> prevFileNames = panti.getImage();
                            for (String prevFileName : prevFileNames) {
                                Path prevFilePath = Paths.get(applicationConfig.getImageUploadDir(), prevFileName);
                                if (Files.exists(prevFilePath)) {
                                    Files.delete(prevFilePath);
                                }
                            }
                        }
                        Files.write(filePath, image.getBytes());
                        imagePaths.add(fileName);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to save image", e);
                    }
                }
                panti.setImage(imagePaths);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save images", e);
        }
    }

    public PantiResponse updatePanti(BigInteger id, UpdatePantiRequest request) {
        UserApp user = userRepository.findById(id).get();
        if(user != null){
            user.setName(request.getName());
            user.setEmail(request.getEmail());
        }

        Panti panti = pantiRepository.findByUserId(id);
        if(panti != null){
            processImage(request, panti);
            processQris(request, panti);
            panti.setDescription(request.getDescription());
            panti.setPhone(request.getPhone());
            panti.setDonationTypes(request.getDonationTypes());
            panti.setIsUrgent(Integer.parseInt(request.getIsUrgent()));
            panti.setAddress(request.getAddress());
            pantiRepository.save(panti);
        }
        return createPantiResponse(user, panti, null);
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
