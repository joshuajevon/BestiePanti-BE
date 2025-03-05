package com.app.bestiepanti.controller;

import java.math.BigInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.donation.CreateNonFundDonationRequest;
import com.app.bestiepanti.dto.response.donation.nonfund.NonFundDonationResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.service.NonFundDonationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/donation/nonfund")
public class NonFundDonationController {
    
    public static final String CREATE_NON_FUND_DONATION_ENDPOINT = "/create/{pantiId}";

    private final NonFundDonationService nonFundDonationService;

    @RequestMapping(value = CREATE_NON_FUND_DONATION_ENDPOINT, method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<NonFundDonationResponse> createNonFundDonation(@Valid @RequestBody CreateNonFundDonationRequest request, @PathVariable BigInteger pantiId) throws UserNotFoundException {
       NonFundDonationResponse nonFundDonationResponse = nonFundDonationService.createNonFundDonation(request, pantiId);
       log.info("Non Fund donation from user id " + nonFundDonationResponse.getDonaturId() + " for panti id "  + pantiId + " is created!");
       return new ResponseEntity<>(nonFundDonationResponse, HttpStatus.CREATED);
    }
}
