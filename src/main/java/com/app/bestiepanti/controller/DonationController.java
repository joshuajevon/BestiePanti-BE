package com.app.bestiepanti.controller;

import java.math.BigInteger;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.donation.CreateDonationRequest;
import com.app.bestiepanti.dto.response.GeneralResponse;
import com.app.bestiepanti.dto.response.donation.DonationResponse;
import com.app.bestiepanti.dto.response.donation.DonationResponses;
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
    public static final String VIEW_ALL_DONATIONS_ENDPOINT = "/view";
    public static final String DELETE_DONATION_ENDPOINT = "/delete/{id}";
    public static final String VIEW_DONATION_BY_USER_ID_ENDPOINT = "/view/{userId}";
    public static final String VERIFY_DONATION_ENDPOINT = "/verify/{id}";

    private final DonationService donationService;

    @RequestMapping(value = CREATE_DONATION_ENDPOINT, method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DonationResponse> createDonation(@Valid @ModelAttribute CreateDonationRequest request, @PathVariable BigInteger pantiId) throws UserNotFoundException {
       DonationResponse donationResponse = donationService.createDonation(request, pantiId);
       log.info("Donation from user id " + donationResponse.getDonaturId() + " for panti id "  + pantiId + " is created!");
       return new ResponseEntity<>(donationResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = VIEW_ALL_DONATIONS_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<DonationResponses> viewAllDonations() {
        DonationResponses donationResponses = new DonationResponses();
        List<DonationResponse> donationResponsesList = donationService.viewAllPanti();
        donationResponses.setDonationResponses(donationResponsesList);
        log.info("Response Body: " + donationResponses);
        return new ResponseEntity<>(donationResponses, HttpStatus.OK);
    }

    @RequestMapping(value = DELETE_DONATION_ENDPOINT, method=RequestMethod.DELETE)
    public ResponseEntity<GeneralResponse> deleteDonation(@PathVariable BigInteger id) {
        donationService.deleteDonation(id);
        GeneralResponse generalResponse = new GeneralResponse("Donation with ID " + id + " has been successfully deleted");
        log.info("Donation " + id + " is deleted!");
        return new ResponseEntity<>(generalResponse,HttpStatus.OK);
    }

    @RequestMapping(value = VIEW_DONATION_BY_USER_ID_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<DonationResponses> viewDonationByUserId(@PathVariable BigInteger userId) throws UserNotFoundException {
        DonationResponses donationResponses = new DonationResponses();
        List<DonationResponse> donationResponsesList = donationService.viewDonationByUserId(userId);
        donationResponses.setDonationResponses(donationResponsesList);
        log.info("Response Body: " + donationResponses);
        return new ResponseEntity<>(donationResponses, HttpStatus.OK);
    }
    
    
    
    
}
