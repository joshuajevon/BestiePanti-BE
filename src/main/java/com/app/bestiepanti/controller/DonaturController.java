package com.app.bestiepanti.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.donatur.UpdateDonaturRequest;
import com.app.bestiepanti.dto.response.GeneralResponse;
import com.app.bestiepanti.dto.response.donatur.DonaturResponse;
import com.app.bestiepanti.dto.response.donatur.DonaturResponses;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.service.DonaturService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;



@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/donatur")
public class DonaturController {
   
    public static final String UPDATE_DONATUR_ENDPOINT = "/update/{id}";
    public static final String DELETE_DONATUR_ENDPOINT = "/delete/{id}";
    public static final String VIEW_ALL_DONATUR_ENDPOINT = "/view";
    public static final String VIEW_DONATUR_BY_ID_ENDPOINT = "/view/{id}";

    @Autowired
    private DonaturService donaturService;

    @RequestMapping(value = UPDATE_DONATUR_ENDPOINT, method=RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DonaturResponse> updateDonatur(@PathVariable BigInteger id, @Valid @ModelAttribute UpdateDonaturRequest request) throws UserNotFoundException {
        DonaturResponse donaturResponse= donaturService.updateDonatur(id, request);
        return new ResponseEntity<>(donaturResponse, HttpStatus.OK);
    }

    @RequestMapping(value = DELETE_DONATUR_ENDPOINT, method=RequestMethod.DELETE)
    public ResponseEntity<GeneralResponse> deleteDonatur(@PathVariable BigInteger id) throws IOException, UserNotFoundException {
        donaturService.deleteDonatur(id);
        GeneralResponse generalResponse = new GeneralResponse("Donatur with ID " + id + " has been successfully deleted");
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @RequestMapping(value = VIEW_ALL_DONATUR_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<DonaturResponses> viewAllDonatur() {
        DonaturResponses donaturResponses = new DonaturResponses();
        List<DonaturResponse> donaturResponseList = donaturService.viewAllDonatur();
        donaturResponses.setDonaturResponses(donaturResponseList);
        return new ResponseEntity<>(donaturResponses, HttpStatus.OK);
    }

    @RequestMapping(value = VIEW_DONATUR_BY_ID_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<DonaturResponse> viewDonaturById(@PathVariable BigInteger id) throws UserNotFoundException {
        DonaturResponse donaturResponse = donaturService.viewDonaturById(id);
        return new ResponseEntity<>(donaturResponse, HttpStatus.OK);
    }
    
    
}
