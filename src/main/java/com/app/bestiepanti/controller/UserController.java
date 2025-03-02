package com.app.bestiepanti.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.auth.LoginRequest;
import com.app.bestiepanti.dto.request.auth.RegisterRequest;
import com.app.bestiepanti.dto.response.donatur.DonaturResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.service.UserService;

import jakarta.validation.Valid;

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

    private final UserService userService;
    
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
    
    
}