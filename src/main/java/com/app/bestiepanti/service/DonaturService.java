package com.app.bestiepanti.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.bestiepanti.dto.request.donatur.UpdateDonaturRequest;
import com.app.bestiepanti.dto.response.donatur.DonaturResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.Donatur;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonaturRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DonaturService {
 
    private final UserRepository userRepository;
    private final DonaturRepository donaturRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-dd-MM");

    public DonaturResponse updateDonatur(BigInteger id, UpdateDonaturRequest request) throws UserNotFoundException {
        UserApp user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " Not Found"));
    
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        Donatur donatur = donaturRepository.findByUserId(id);
        if(donatur != null){
            LocalDate dob = LocalDate.parse(request.getDob(), formatter);
            donatur.setDob(dob);
            donatur.setGender(request.getGender());
            donatur.setPhone(request.getPhone());
            donatur.setAddress(request.getAddress());
            donaturRepository.save(donatur);
        }
        return createDonaturResponse(user, donatur);
    }
    
    @Transactional
    public void deleteDonatur(BigInteger id) throws UserNotFoundException {
        if (!userRepository.existsById(id)) throw new UserNotFoundException("User with id " + id + " Not Found");
        try {
            donaturRepository.deleteByUserId(id);
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Donatur with id " + id, e);
        }
    }

    public DonaturResponse createDonaturResponse(UserApp userApp, Donatur donatur) {
        return DonaturResponse.builder()
                .id(userApp.getId())
                .name(userApp.getName())
                .email(userApp.getEmail())
                .role(userApp.getRole().getName())
                .dob(donatur.getDob().toString())
                .gender(donatur.getGender())
                .phone(donatur.getPhone())
                .address(donatur.getAddress())
                .build();
    }

    public List<DonaturResponse> viewAllDonatur() {
        List<Donatur> donaturList = donaturRepository.findAll();
        List<DonaturResponse> donaturResponseList = new ArrayList<>();
        for (Donatur donatur : donaturList) {
            DonaturResponse donaturResponse = createDonaturResponse(donatur.getUser(), donatur);
            donaturResponseList.add(donaturResponse);
        }
        return donaturResponseList;
    }

}
