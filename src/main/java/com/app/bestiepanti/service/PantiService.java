package com.app.bestiepanti.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public PantiResponse createPanti(PantiRequest request){
        UserApp user = new UserApp();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
 
        Role role = roleRepository.findByName(UserApp.ROLE_PANTI);
        user.setRole(role);
 
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
 
        Panti panti = saveToPanti(request, user);
        return createPantiResponse(user, panti);
    }
 
    public Panti saveToPanti(PantiRequest request, UserApp user){
        Panti panti = new Panti();
        panti.setImage(request.getImage());
        panti.setDescription(request.getDescription());
        panti.setPhone(request.getPhone());
        panti.setDonationTypes(request.getDonationTypes());
        panti.setIsUrgent(request.getIsUrgent());
        panti.setAddress(request.getAddress());
        panti.setQris(request.getQris());
        panti.setUser(user);
        pantiRepository.save(panti);
        return panti;
    }

    public PantiResponse createPantiResponse(UserApp userApp, Panti panti) {
        return PantiResponse.builder()
                .id(userApp.getId())
                .name(userApp.getName())
                .email(userApp.getEmail())
                .role(userApp.getRole().getName())
                .image(panti.getImage())
                .phone(panti.getPhone())
                .donationTypes(panti.getDonationTypes())
                .isUrgent(panti.getIsUrgent())
                .address(panti.getAddress())
                .qris(panti.getQris())
                .build();
    }
   
}
