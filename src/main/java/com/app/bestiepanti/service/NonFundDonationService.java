package com.app.bestiepanti.service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.bestiepanti.dto.request.donation.CreateNonFundDonationRequest;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserApp userPanti = userRepository.findById(pantiId).orElseThrow(() -> new UserNotFoundException("User with id " + pantiId + " Not Found"));
        String email = authentication.getName();
        UserApp userDonatur = userService.findUserByEmail(email);

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
