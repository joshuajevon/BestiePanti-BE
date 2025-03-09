package com.app.bestiepanti.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.auth.LoginRequest;
import com.app.bestiepanti.dto.request.auth.RegisterRequest;
import com.app.bestiepanti.dto.request.donatur.UpdateDonaturRequest;
import com.app.bestiepanti.dto.request.panti.UpdatePantiRequest;
import com.app.bestiepanti.dto.response.GeneralResponse;
import com.app.bestiepanti.dto.response.donatur.DonaturResponse;
import com.app.bestiepanti.dto.response.panti.PantiResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.service.DonaturService;
import com.app.bestiepanti.service.PantiService;
import com.app.bestiepanti.service.UserService;

import jakarta.validation.Valid;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    
    public static final String REGISTER_ENDPOINT = "/register";
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String USER_ENDPOINT = "/user";
    public static final String UPDATE_DONATUR_ENDPOINT = "/donatur/profile/update";
    public static final String UPDATE_PANTI_ENDPOINT = "/panti/profile/update";
    public static final String DELETE_USER_ENDPOINT = "/profile/delete";

    private final UserService userService;
    private final DonaturService donaturService;
    private final PantiService pantiService;
    
    @RequestMapping(value = REGISTER_ENDPOINT, method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<DonaturResponse> register(@Valid @RequestBody RegisterRequest userRequest){     
        DonaturResponse donaturResponse = userService.register(userRequest);
        log.info("Donatur " + donaturResponse.getId() + " is registered!");
        return new ResponseEntity<>(donaturResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = LOGIN_ENDPOINT, method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest userRequest) throws UserNotFoundException{
        Object userResponse = userService.login(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @RequestMapping(value = USER_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<Object> getUser() throws UserNotFoundException {
        Object userResponse = userService.getUser();
        log.info("Response Body: " + userResponse);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
    
    @RequestMapping(value = UPDATE_DONATUR_ENDPOINT, method=RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DonaturResponse> updateDonaturProfile(@Valid @ModelAttribute UpdateDonaturRequest request) throws UserNotFoundException {
        UserApp user = userService.getAuthenticate();
        DonaturResponse donaturResponse = donaturService.updateDonatur(user.getId(), request);
        log.info("Response Body: " + donaturResponse);
        return new ResponseEntity<>(donaturResponse, HttpStatus.OK);
    }
    
    @RequestMapping(value = UPDATE_PANTI_ENDPOINT, method=RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PantiResponse> updatePantiProfile(@Valid @ModelAttribute UpdatePantiRequest request) throws UserNotFoundException {
        UserApp user = userService.getAuthenticate();
        PantiResponse pantiResponse = pantiService.updatePanti(user.getId(), request);
        log.info("Response Body: " + pantiResponse);
        return new ResponseEntity<>(pantiResponse, HttpStatus.OK);
    }

    @RequestMapping(value = DELETE_USER_ENDPOINT, method=RequestMethod.DELETE)
    public ResponseEntity<Object> deleteUserProfile() throws UserNotFoundException, IOException {
        UserApp user = userService. getAuthenticate();
        if(user.getRole().getName().equals(UserApp.ROLE_DONATUR))
            donaturService.deleteDonatur(user.getId());
        else if(user.getRole().getName().equals(UserApp.ROLE_PANTI))
            pantiService.deletePanti(user.getId());
        GeneralResponse generalResponse = new GeneralResponse("User with ID " + user.getId() + " has been successfully deleted");
        log.info("User " + user.getId() + " is deleted!");
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }
}