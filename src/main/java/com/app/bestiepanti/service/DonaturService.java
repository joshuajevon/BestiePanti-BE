package com.app.bestiepanti.service;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.bestiepanti.configuration.ApplicationConfig;
import com.app.bestiepanti.dto.request.donatur.ProfileDonaturRequest;
import com.app.bestiepanti.dto.request.donatur.UpdateDonaturRequest;
import com.app.bestiepanti.dto.response.donatur.DonaturResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.Donation;
import com.app.bestiepanti.model.Donatur;
import com.app.bestiepanti.model.Message;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonationRepository;
import com.app.bestiepanti.repository.DonaturRepository;
import com.app.bestiepanti.repository.MessageRepository;
import com.app.bestiepanti.repository.TwoStepVerificationRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DonaturService {
 
    private final UserRepository userRepository;
    private final DonaturRepository donaturRepository;
    private final FundDonationService fundDonationService;
    private final NonFundDonationService nonFundDonationService;
    private final DonationRepository donationRepository;
    private final MessageService messageService;
    private final MessageRepository messageRepository;
    private final TwoStepVerificationRepository twoStepVerificationRepository;
    private final ApplicationConfig applicationConfig;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DonaturResponse updateDonatur(BigInteger id, UpdateDonaturRequest request) throws UserNotFoundException {
        UserApp user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " Not Found"));
    
        user.setName(request.getName());

        Donatur donatur = donaturRepository.findByUserId(id);
        if(donatur != null){
            processProfile(request, donatur, request.getGender());
            LocalDate dob = LocalDate.parse(request.getDob(), formatter);
            donatur.setDob(dob);
            donatur.setGender(request.getGender());
            donatur.setPhone("+62"+request.getPhone());
            donatur.setAddress(request.getAddress());
            donaturRepository.save(donatur);
        }
        return createDonaturResponse(user, donatur);
    }
    
    @Transactional
    public void deleteDonatur(BigInteger id) throws UserNotFoundException {
        if (!userRepository.existsById(id)) throw new UserNotFoundException("User with id " + id + " Not Found");
        try {
            Donatur donatur = donaturRepository.findByUserId(id);
            if(donatur != null){
                if (donatur.getProfile() != null && !(donatur.getProfile().equals("defaultProfileMale.png") || donatur.getProfile().equals("defaultProfileFemale.png"))) {
                    String fileQris = donatur.getProfile();
                    Path filePath = Paths.get(applicationConfig.getProfileImageUploadDir(), fileQris);
                    if (Files.exists(filePath)) {
                        Files.delete(filePath);
                    }
                }
            }
            
            List<Donation> donations = donationRepository.findAllByDonaturId(id);
            if(!donations.isEmpty()){
                for (Donation donation : donations) {
                    if(donation.getDonationTypes().contains("Dana"))
                        fundDonationService.deleteFundDonation(donation.getId());
                    else
                        nonFundDonationService.deleteNonFundDonation(donation.getId());
                }
            }

            List<Message> messages = messageRepository.findAllByDonaturId(id);
            if(!messages.isEmpty()){
                for (Message message : messages) {
                    messageService.deleteMessage(message.getId());
                }
            }

            twoStepVerificationRepository.deleteByEmail(donatur.getUser().getEmail());
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
                .phone(donatur != null ? donatur.getPhone(): null)
                .dob(donatur != null ? Optional.ofNullable(donatur.getDob()).map(LocalDate::toString).orElse(null) : null)
                .gender(donatur != null ? donatur.getGender() : null)
                .address(donatur != null ? donatur.getAddress() : null)
                .profile(donatur != null ? donatur.getProfile() : null)
                .build();
    }

    public List<DonaturResponse> viewAllDonatur() {
        List<Donatur> donaturList = donaturRepository.findAllByOrderByIdDesc();
        List<DonaturResponse> donaturResponseList = new ArrayList<>();
        for (Donatur donatur : donaturList) {
            DonaturResponse donaturResponse = createDonaturResponse(donatur.getUser(), donatur);
            donaturResponseList.add(donaturResponse);
        }
        return donaturResponseList;
    }

    public DonaturResponse viewDonaturById(BigInteger id) throws UserNotFoundException {
        DonaturResponse donaturResponse = new DonaturResponse();
        Donatur donatur = donaturRepository.findByUserId(id);
        if(donatur == null){
            throw new UserNotFoundException("Donatur with id " + id + " Not Found");
        } else {
            donaturResponse = createDonaturResponse(donatur.getUser(), donatur);
        }
        return donaturResponse;
    }

    private void processProfile(ProfileDonaturRequest request, Donatur donatur, String gender) {
        try {
            if (request.getProfile() != null && !request.getProfile().isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + donatur.getUser().getId() + "_" + request.getProfile().getOriginalFilename();
                Path filePath = Paths.get(applicationConfig.getProfileImageUploadDir(), fileName);
                try {
                    if(donatur.getProfile() != null && !(donatur.getProfile().equals("defaultProfileMale.png") || donatur.getProfile().equals("defaultProfileFemale.png") || donatur.getProfile().equals("defaultProfileGeneral.png"))){
                        String prevFileName = donatur.getProfile();
                        Path prevFilePath = Paths.get(applicationConfig.getProfileImageUploadDir(), prevFileName);
                        if (Files.exists(prevFilePath)) {
                            Files.delete(prevFilePath);
                        }
                    }
                    Files.write(filePath, request.getProfile().getBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to save profile", e);
                }
                donatur.setProfile(fileName);
            } else {
                if(gender.equals("L")) donatur.setProfile("defaultProfileMale.png");
                else if(gender.equals("P")) donatur.setProfile("defaultProfileFemale.png");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save profile", e);
        }
    }

}
