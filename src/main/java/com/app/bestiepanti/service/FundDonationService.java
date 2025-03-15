package com.app.bestiepanti.service;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.app.bestiepanti.configuration.ApplicationConfig;
import com.app.bestiepanti.dto.request.donation.fund.CreateFundDonationRequest;
import com.app.bestiepanti.dto.request.donation.fund.ImageFundDonationRequest;
import com.app.bestiepanti.dto.request.donation.fund.UpdateFundDonationRequest;
import com.app.bestiepanti.dto.response.donation.fund.FundDonationResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.Donation;
import com.app.bestiepanti.model.Fund;
import com.app.bestiepanti.model.Panti;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonationRepository;
import com.app.bestiepanti.repository.FundDonationRepository;
import com.app.bestiepanti.repository.PantiRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundDonationService {

    private final DonationRepository donationRepository;
    private final PantiRepository pantiRepository;
    private final ApplicationConfig applicationConfig;
    private final UserRepository userRepository;
    private final UserService userService;
    private final FundDonationRepository fundDonationRepository;

    public FundDonationResponse createFundDonation(CreateFundDonationRequest request, BigInteger pantiId) throws UserNotFoundException{
        UserApp userDonatur = userService.getAuthenticate();
        UserApp userPanti = userRepository.findById(pantiId).orElseThrow(() -> new UserNotFoundException("User with id " + pantiId + " Not Found"));
        Panti panti = pantiRepository.findByUserId(pantiId);
        Donation donation = new Donation();
        LocalDate fundDonationDate = LocalDate.now();
        donation.setDonationDate(fundDonationDate);
        donation.setDonationTypes(Arrays.asList("Dana"));
        donation.setDonaturId(userDonatur);
        donation.setPantiId(userPanti);
        donation.setIsOnsite(0);
        donation.setStatus(Donation.STATUS_PENDING);
        donation.setInsertedTimestamp(LocalDateTime.now());
        donationRepository.save(donation);

        Fund fund = new Fund();
        fund.setAccountNumber(request.getAccountNumber());
        fund.setAccountName(request.getAccountName());
        fund.setNominalAmount(request.getNominalAmount());
        fund.setDonationId(donation);
        processImage(request, fund);
        fundDonationRepository.save(fund);
        
        return createFundDonationResponse(donation, fund, panti);
    }

    public List<FundDonationResponse> viewAllFundDonation() {
        List<Donation> donationList = donationRepository.findAllByFundTypes();
        List<FundDonationResponse> fundDonationResponseList = new ArrayList<>();
        for (Donation donation : donationList) {
            Panti panti = pantiRepository.findByUserId(donation.getPantiId().getId());
            Fund fund = fundDonationRepository.findByDonationId(donation.getId());
            FundDonationResponse fundDonationResponse = createFundDonationResponse(donation, fund, panti);
            fundDonationResponseList.add(fundDonationResponse);
        }
        return fundDonationResponseList;
    }

    public void deleteFundDonation(BigInteger id){

        try {
            Donation donation = donationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Fund Donation with id " + id + " Not Found"));
            Fund fund = fundDonationRepository.findByDonationId(id);
            if(fund != null && fund.getImage() != null){
                String fileName = fund.getImage();
                Path filePath = Paths.get(applicationConfig.getImageDonationUploadDir(), fileName);
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
            }
            fundDonationRepository.delete(fund);
            donationRepository.delete(donation);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Fund Donation with id " + id, e);
        }
    }

    public List<FundDonationResponse> viewFundDonationByUserId(BigInteger userId) throws UserNotFoundException{
        UserApp user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " Not Found"));
        List<FundDonationResponse> fundDonationResponses = new ArrayList<>();
        List<Donation> donations = new ArrayList<>();
        if(user.getRole().getName().equals(UserApp.ROLE_DONATUR)){
            donations = donationRepository.findAllByDonaturIdAndFundTypes(userId);
        } else if (user.getRole().getName().equals(UserApp.ROLE_PANTI)){
            donations = donationRepository.findAllByPantiIdAndFundTypes(userId);
        }

        if(!donations.isEmpty()){
            for (Donation donation : donations) {
                Panti panti = pantiRepository.findByUserId(donation.getPantiId().getId());
                Fund fund = fundDonationRepository.findByDonationId(donation.getId());
                FundDonationResponse response = createFundDonationResponse(donation, fund, panti);
                fundDonationResponses.add(response);
            }
        }
        return fundDonationResponses;
    }

    public FundDonationResponse verifyFundDonation(BigInteger id, UpdateFundDonationRequest request) throws UserNotFoundException{
        try {  
            Donation donation = donationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Fund Donation with id " + id + " Not Found"));
            Panti panti = pantiRepository.findByUserId(donation.getPantiId().getId());
            UserApp userPanti = userService.getAuthenticate();
            if(userPanti.getId() != donation.getPantiId().getId()){
                throw new UserNotFoundException("User is not permitted to verify this fund donation");
            }

            donation.setStatus(request.getStatus());
            donation.setVerifiedTimestamp(LocalDateTime.now());
            donationRepository.save(donation);
            
            Fund fund = fundDonationRepository.findByDonationId(id);
            if(fund != null){
                processImage(request, fund);
                fund.setAccountName(request.getAccountName());
                fund.setAccountNumber(request.getAccountNumber());
                fund.setNominalAmount(request.getNominalAmount());
                fundDonationRepository.save(fund);
            }
            return createFundDonationResponse(donation, fund, panti);
        } catch (NumberFormatException e) {
            throw e;
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

        
    public FundDonationResponse createFundDonationResponse(Donation donation, Fund fund, Panti panti) {
        return FundDonationResponse.builder()
                .id(donation.getId())
                .donaturId(donation.getDonaturId().getId())
                .donaturName(donation.getDonaturId().getName())
                .pantiId(donation.getPantiId().getId())
                .pantiName(donation.getPantiId().getName())
                .donationDate(donation.getDonationDate().toString())
                .isOnsite(donation.getIsOnsite())
                .donationTypes(donation.getDonationTypes())
                .image(fund.getImage())
                .accountName(fund.getAccountName())
                .accountNumber(fund.getAccountNumber())
                .nominalAmount(fund.getNominalAmount())
                .status(donation.getStatus())
                .profile(panti.getImage().getFirst())
                .insertedTimestamp(donation.getInsertedTimestamp())
                .verifiedTimestamp(donation.getVerifiedTimestamp())
                .build();
    }
    
    public void processImage(ImageFundDonationRequest request, Fund fund) {
        try {
            if (request.getImage() != null && !request.getImage().isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + fund.getDonationId().getPantiId().getName() + "_" + request.getImage().getOriginalFilename();
                Path filePath = Paths.get(applicationConfig.getImageDonationUploadDir(), fileName);
                    
                    try {
                        if(fund.getImage() != null){
                            String prevFileName = fund.getImage();
                            Path prevFilePath = Paths.get(applicationConfig.getImageDonationUploadDir(), prevFileName);
                            if (Files.exists(prevFilePath)) {
                                Files.delete(prevFilePath);
                            }
                        }
                        Files.write(filePath, request.getImage().getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to save image", e);
                    }
                    fund.setImage(fileName);
                }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save images", e);
        }
    }
}