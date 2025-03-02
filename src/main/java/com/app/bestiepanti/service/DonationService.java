package com.app.bestiepanti.service;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.bestiepanti.configuration.ApplicationConfig;
import com.app.bestiepanti.dto.request.donation.CreateDonationRequest;
import com.app.bestiepanti.dto.request.donation.ImageDonationRequest;
import com.app.bestiepanti.dto.response.donation.DonationResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.Donation;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonationRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final ApplicationConfig applicationConfig;
    private final UserRepository userRepository;
    private final UserService userService;

    public DonationResponse createDonation(CreateDonationRequest request, BigInteger pantiId) throws UserNotFoundException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserApp userPanti = userRepository.findById(pantiId).orElseThrow(() -> new UserNotFoundException("User with id " + pantiId + " Not Found"));
        String email = authentication.getName();
        UserApp userDonatur = userService.findUserByEmail(email);

        Donation donation = new Donation();
        LocalDate donationDate = LocalDate.parse(request.getDonationDate());
        donation.setDonationDate(donationDate);
        donation.setDonationTypes(request.getDonationTypes());
        donation.setDonaturId(userDonatur);
        donation.setPantiId(userPanti);
        donation.setIsOnsite(Integer.parseInt(request.getIsOnsite()));
        donation.setNotes(request.getNotes());
        donation.setStatus(Donation.STATUS_PENDING);
        processImage(request, donation);
        donationRepository.save(donation);
        
        return createDonationResponse(donation);
    }

    public List<DonationResponse> viewAllPanti() {
        List<Donation> donationList = donationRepository.findAll();
        List<DonationResponse> donationResponseList = new ArrayList<>();
        for (Donation donation : donationList) {
            DonationResponse donationResponse = createDonationResponse(donation);
            donationResponseList.add(donationResponse);
        }
        return donationResponseList;
    }

        
    public DonationResponse createDonationResponse(Donation donation) {
        return DonationResponse.builder()
                .id(donation.getId())
                .donaturId(donation.getDonaturId().getId())
                .pantiId(donation.getPantiId().getId())
                .donationDate(donation.getDonationDate().toString())
                .isOnsite(donation.getIsOnsite())
                .donationTypes(donation.getDonationTypes())
                .images(donation.getImage())
                .notes(donation.getNotes())
                .status(donation.getStatus())
                .build();
    }
    
    public void processImage(ImageDonationRequest request, Donation donation) {
        try {
            if (request.getImages() != null && !request.getImages().isEmpty()) {
                List<String> imagePaths = new ArrayList<>();
                for (MultipartFile image : request.getImages()) {
                    String fileName = System.currentTimeMillis() + "_" + donation.getPantiId().getName() + "_" + image.getOriginalFilename();
                    Path filePath = Paths.get(applicationConfig.getImageDonationUploadDir(), fileName);
                    
                    try {
                        if(donation.getImage() != null){
                            List<String> prevFileNames = donation.getImage();
                            for (String prevFileName : prevFileNames) {
                                Path prevFilePath = Paths.get(applicationConfig.getImageDonationUploadDir(), prevFileName);
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
                donation.setImage(imagePaths);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save images", e);
        }
    }
}
