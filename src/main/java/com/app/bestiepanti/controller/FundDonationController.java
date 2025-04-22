package com.app.bestiepanti.controller;

import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.donation.fund.CreateFundDonationRequest;
import com.app.bestiepanti.dto.request.donation.fund.UpdateFundDonationRequest;
import com.app.bestiepanti.dto.response.GeneralResponse;
import com.app.bestiepanti.dto.response.donation.fund.FundDonationResponse;
import com.app.bestiepanti.dto.response.donation.fund.FundDonationResponses;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.service.FundDonationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/donation/fund")
public class FundDonationController {

    public static final String CREATE_FUND_DONATION_ENDPOINT = "/create/{pantiId}";
    public static final String VIEW_ALL_FUND_DONATIONS_ENDPOINT = "/view";
    public static final String DELETE_FUND_DONATION_ENDPOINT = "/delete/{id}";
    public static final String VIEW_FUND_DONATION_BY_USER_ID_ENDPOINT = "/view/{userId}";
    public static final String VERIFY_FUND_DONATION_ENDPOINT = "/verify/{id}";
    public static final String VIEW_FUND_DONATION_BY_DONATION_ID_ENDPOINT = "/get/{donationId}";

    private final FundDonationService fundDonationService;

    @RequestMapping(value = CREATE_FUND_DONATION_ENDPOINT, method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FundDonationResponse> createFundDonation(@Valid @ModelAttribute CreateFundDonationRequest request, @PathVariable BigInteger pantiId) throws Exception {
       FundDonationResponse fundDonationResponse = fundDonationService.createFundDonation(request, pantiId);
       log.info("Fund donation from user id " + fundDonationResponse.getDonaturId() + " for panti id "  + pantiId + " is created and email has been sent!");
       return new ResponseEntity<>(fundDonationResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = VIEW_ALL_FUND_DONATIONS_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<FundDonationResponses> viewAllFundDonations() {
        FundDonationResponses fundDonationResponses = new FundDonationResponses();
        List<FundDonationResponse> fundDonationResponsesList = fundDonationService.viewAllFundDonation();
        fundDonationResponses.setFundDonationResponses(fundDonationResponsesList);
        log.info("Response Body: " + fundDonationResponses);
        return new ResponseEntity<>(fundDonationResponses, HttpStatus.OK);
    }

    @RequestMapping(value = DELETE_FUND_DONATION_ENDPOINT, method=RequestMethod.DELETE)
    public ResponseEntity<GeneralResponse> deleteFundDonation(@PathVariable BigInteger id) {
        try {
            fundDonationService.deleteFundDonation(id);
            GeneralResponse generalResponse = new GeneralResponse("Fund Donation with ID " + id + " has been successfully deleted");
            log.info("Fund donation " + id + " is deleted!");
            return new ResponseEntity<>(generalResponse,HttpStatus.OK);
        } catch (NoSuchElementException e) {
            GeneralResponse generalResponse = new GeneralResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generalResponse);
        } 
    }

    @RequestMapping(value = VIEW_FUND_DONATION_BY_USER_ID_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<FundDonationResponses> viewFundDonationByUserId(@PathVariable BigInteger userId) throws UserNotFoundException {
        FundDonationResponses fundDonationResponses = new FundDonationResponses();
        List<FundDonationResponse> fundDonationResponsesList = fundDonationService.viewFundDonationByUserId(userId);
        fundDonationResponses.setFundDonationResponses(fundDonationResponsesList);
        log.info("Response Body: " + fundDonationResponses);
        return new ResponseEntity<>(fundDonationResponses, HttpStatus.OK);
    }
    
    @RequestMapping(value = VERIFY_FUND_DONATION_ENDPOINT, method=RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> verifyFundDonation(@PathVariable BigInteger id, @Valid @ModelAttribute UpdateFundDonationRequest request) throws Exception {
        try {
            log.info("Request Body: " + request.toString());
            FundDonationResponse fundDonationResponse = fundDonationService.verifyFundDonation(id, request);
            log.info("Fund donation with id " + id + " has been verified and email has been sent!");
            return new ResponseEntity<>(fundDonationResponse, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            GeneralResponse generalResponse = new GeneralResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generalResponse);
        }
    }

    @RequestMapping(value = VIEW_FUND_DONATION_BY_DONATION_ID_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<FundDonationResponse> viewFundDonationById(@PathVariable BigInteger donationId) throws UserNotFoundException {
        FundDonationResponse fundDonationResponse = fundDonationService.viewFundDonationByDonationId(donationId);
        log.info("Response Body: " + fundDonationResponse);
        return new ResponseEntity<>(fundDonationResponse, HttpStatus.OK);
    }
    
}
