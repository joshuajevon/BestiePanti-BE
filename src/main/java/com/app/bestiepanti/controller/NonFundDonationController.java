package com.app.bestiepanti.controller;

import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.donation.nonfund.UpdateNonFundDonationRequest;
import com.app.bestiepanti.dto.request.donation.nonfund.CreateNonFundDonationRequest;
import com.app.bestiepanti.dto.response.GeneralResponse;
import com.app.bestiepanti.dto.response.donation.nonfund.NonFundDonationResponse;
import com.app.bestiepanti.dto.response.donation.nonfund.NonFundDonationResponses;
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
    public static final String VIEW_ALL_NON_FUND_DONATIONS_ENDPOINT = "/view";
    public static final String DELETE_NON_FUND_DONATION_ENDPOINT = "/delete/{id}";
    public static final String VIEW_NON_FUND_DONATION_BY_USER_ID_ENDPOINT = "/view/{userId}";
    public static final String VERIFY_NON_FUND_DONATION_ENDPOINT = "/verify/{id}";
    public static final String VIEW_NON_FUND_DONATION_BY_DONATION_ID_ENDPOINT = "/get/{donationId}";

    private final NonFundDonationService nonFundDonationService;

    @RequestMapping(value = CREATE_NON_FUND_DONATION_ENDPOINT, method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<NonFundDonationResponse> createNonFundDonation(@Valid @RequestBody CreateNonFundDonationRequest request, @PathVariable BigInteger pantiId) throws UserNotFoundException {
       NonFundDonationResponse nonFundDonationResponse = nonFundDonationService.createNonFundDonation(request, pantiId);
       log.info("Non Fund donation from user id " + nonFundDonationResponse.getDonaturId() + " for panti id "  + pantiId + " is created!");
       return new ResponseEntity<>(nonFundDonationResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = VIEW_ALL_NON_FUND_DONATIONS_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<NonFundDonationResponses> viewAllFundDonations() {
        NonFundDonationResponses nonFundDonationResponses = new NonFundDonationResponses();
        List<NonFundDonationResponse> nonFundDonationResponsesList = nonFundDonationService.viewAllNonFundDonation();
        nonFundDonationResponses.setNonFundDonationResponse(nonFundDonationResponsesList);
        log.info("Response Body: " + nonFundDonationResponses);
        return new ResponseEntity<>(nonFundDonationResponses, HttpStatus.OK);
    }

    @RequestMapping(value = DELETE_NON_FUND_DONATION_ENDPOINT, method=RequestMethod.DELETE)
    public ResponseEntity<GeneralResponse> deleteFundDonation(@PathVariable BigInteger id) {
        try {
            nonFundDonationService.deleteNonFundDonation(id);
            GeneralResponse generalResponse = new GeneralResponse("Non Fund Donation with ID " + id + " has been successfully deleted");
            log.info("Non Fund donation " + id + " is deleted!");
            return new ResponseEntity<>(generalResponse,HttpStatus.OK);
        } catch (NoSuchElementException e) {
            GeneralResponse generalResponse = new GeneralResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generalResponse);
        } 
    }

    @RequestMapping(value = VIEW_NON_FUND_DONATION_BY_USER_ID_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<NonFundDonationResponses> viewNonFundDonationByUserId(@PathVariable BigInteger userId) throws UserNotFoundException {
        NonFundDonationResponses nonFundDonationResponses = new NonFundDonationResponses();
        List<NonFundDonationResponse> nonFundDonationResponsesList = nonFundDonationService.viewNonFundDonationByUserId(userId);
        nonFundDonationResponses.setNonFundDonationResponse(nonFundDonationResponsesList);
        log.info("Response Body: " + nonFundDonationResponses);
        return new ResponseEntity<>(nonFundDonationResponses, HttpStatus.OK);
    }
    
    @RequestMapping(value = VERIFY_NON_FUND_DONATION_ENDPOINT, method=RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> verifyFundDonation(@PathVariable BigInteger id, @Valid @RequestBody UpdateNonFundDonationRequest request) throws UserNotFoundException {
        try {
            log.info("Request Body: " + request.toString());
            NonFundDonationResponse nonFundDonationResponse = nonFundDonationService.verifyNonFundDonation(id, request);
            log.info("Non Fund donation with id " + id + " has been verified!");
            return new ResponseEntity<>(nonFundDonationResponse, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            GeneralResponse generalResponse = new GeneralResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generalResponse);
        }
    }

    @RequestMapping(value = VIEW_NON_FUND_DONATION_BY_DONATION_ID_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<NonFundDonationResponse> viewNonFundDonationById(@PathVariable BigInteger donationId) throws UserNotFoundException {
        NonFundDonationResponse nonFundDonationResponse = nonFundDonationService.viewNonFundDonationByDonationId(donationId);
        log.info("Response Body: " + nonFundDonationResponse );
        return new ResponseEntity<>(nonFundDonationResponse , HttpStatus.OK);
    }
}
