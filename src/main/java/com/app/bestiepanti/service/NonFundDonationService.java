package com.app.bestiepanti.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.app.bestiepanti.dto.request.donation.nonfund.CreateNonFundDonationRequest;
import com.app.bestiepanti.dto.request.donation.nonfund.UpdateNonFundDonationRequest;
import com.app.bestiepanti.dto.response.donation.nonfund.NonFundDonationResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.Donation;
import com.app.bestiepanti.model.NonFund;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonationRepository;
import com.app.bestiepanti.repository.NonFundDonationRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NonFundDonationService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final DonationRepository donationRepository;
    private final NonFundDonationRepository nonFundDonationRepository;
    
    public NonFundDonationResponse createNonFundDonation(CreateNonFundDonationRequest request, BigInteger pantiId) throws UserNotFoundException{
        UserApp userDonatur = userService.getAuthenticate();
        UserApp userPanti = userRepository.findById(pantiId).orElseThrow(() -> new UserNotFoundException("User with id " + pantiId + " Not Found"));
        Donation donation = new Donation();
        LocalDate fundDonationDate = LocalDate.parse(request.getDonationDate());
        donation.setDonationDate(fundDonationDate);
        donation.setDonationTypes(request.getDonationTypes());
        donation.setDonaturId(userDonatur);
        donation.setPantiId(userPanti);
        donation.setIsOnsite(Integer.parseInt(request.getIsOnsite()));
        donation.setStatus(Donation.STATUS_PENDING);
        donation.setInsertedTimestamp(LocalDateTime.now());
        donationRepository.save(donation);

        NonFund nonFund = new NonFund();
        nonFund.setPic(request.getPic());
        nonFund.setNotes(request.getNotes());
        nonFund.setActivePhone(request.getActivePhone());
        nonFund.setDonationId(donation);
        nonFundDonationRepository.save(nonFund);
        
        return createNonFundDonationResponse(donation, nonFund);
    }

    public List<NonFundDonationResponse> viewAllNonFundDonation() {
        List<Donation> donationList = donationRepository.findAllByNonFundTypes();
        List<NonFundDonationResponse> nonFundDonationResponseList = new ArrayList<>();
        for (Donation donation : donationList) {
            NonFund nonFund = nonFundDonationRepository.findByDonationId(donation.getId());
            NonFundDonationResponse nonFundDonationResponse = createNonFundDonationResponse(donation, nonFund);
            nonFundDonationResponseList.add(nonFundDonationResponse);
        }
        return nonFundDonationResponseList;
    }

    public void deleteNonFundDonation(BigInteger id){

        try {
            Donation donation = donationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Non Fund Donation with id " + id + " Not Found"));
            NonFund nonFund = nonFundDonationRepository.findByDonationId(id);
            if(nonFund == null) throw new NoSuchElementException("Non Fund Donation with id " + id + " Not Found");
            nonFundDonationRepository.delete(nonFund);
            donationRepository.delete(donation);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Non Fund Donation with id " + id, e);
        }
    }

    public List<NonFundDonationResponse> viewNonFundDonationByUserId(BigInteger userId) throws UserNotFoundException{
        UserApp user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " Not Found"));
        List<NonFundDonationResponse> nonFundDonationResponses = new ArrayList<>();
        List<Donation> donations = new ArrayList<>();
        if(user.getRole().getName().equals(UserApp.ROLE_DONATUR)){
            donations = donationRepository.findAllByDonaturIdAndNonFundTypes(userId);
        } else if (user.getRole().getName().equals(UserApp.ROLE_PANTI)){
            donations = donationRepository.findAllByPantiIdAndNonFundTypes(userId);
        }

        if(!donations.isEmpty()){
            for (Donation donation : donations) {
                NonFund nonFund = nonFundDonationRepository.findByDonationId(donation.getId());
                NonFundDonationResponse response = createNonFundDonationResponse(donation, nonFund);
                nonFundDonationResponses.add(response);
            }
        }
        return nonFundDonationResponses;
    }

    public NonFundDonationResponse verifyNonFundDonation(BigInteger id, UpdateNonFundDonationRequest request){
        try {
            Donation donation = donationRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Non Fund Donation with id " + id + " Not Found"));
            donation.setStatus(request.getStatus());
            donation.setVerifiedTimestamp(LocalDateTime.now());
            LocalDate fundDonationDate = LocalDate.parse(request.getDonationDate());
            donation.setDonationDate(fundDonationDate);
            donation.setDonationTypes(request.getDonationTypes());
            donation.setIsOnsite(Integer.parseInt(request.getIsOnsite()));
            donationRepository.save(donation);
            
            NonFund nonFund = nonFundDonationRepository.findByDonationId(id);
            if(nonFund != null){
                nonFund.setPic(request.getPic());
                nonFund.setNotes(request.getNotes());
                nonFund.setActivePhone(request.getActivePhone());
                nonFundDonationRepository.save(nonFund);
            }
            return createNonFundDonationResponse(donation, nonFund);
        } catch (NumberFormatException e) {
            throw e;
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    public NonFundDonationResponse createNonFundDonationResponse(Donation donation, NonFund nonFund) {
        return NonFundDonationResponse.builder()
                .id(donation.getId())
                .donaturId(donation.getDonaturId().getId())
                .pantiId(donation.getPantiId().getId())
                .donationDate(donation.getDonationDate().toString())
                .isOnsite(donation.getIsOnsite())
                .donationTypes(donation.getDonationTypes())
                .activePhone(nonFund.getActivePhone())
                .pic(nonFund.getPic())
                .notes(nonFund.getNotes())
                .status(donation.getStatus())
                .insertedTimestamp(donation.getInsertedTimestamp())
                .verifiedTimestamp(donation.getVerifiedTimestamp())
                .build();
    }
}
