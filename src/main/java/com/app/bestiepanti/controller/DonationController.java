package com.app.bestiepanti.controller;

import java.math.BigInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.donation.CreateDonationRequest;
import com.app.bestiepanti.dto.response.donation.DonationResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.service.DonationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/donation")
public class DonationController {

    public static final String CREATE_DONATION_ENDPOINT = "/create/{pantiId}";

    private final DonationService donationService;

    @RequestMapping(value = CREATE_DONATION_ENDPOINT, method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DonationResponse> createDonation(@Valid @ModelAttribute CreateDonationRequest request, @PathVariable BigInteger pantiId) throws UserNotFoundException {
       DonationResponse donationResponse = donationService.createDonation(request, pantiId);
       log.info("Donation from user id " + donationResponse.getDonaturId() + " for panti id "  + pantiId + " is created!");
       return new ResponseEntity<>(donationResponse, HttpStatus.CREATED);
    }
    
}
