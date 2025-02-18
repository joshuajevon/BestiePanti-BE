package com.app.bestiepanti.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.LoginRequest;
import com.app.bestiepanti.dto.request.RegisterRequest;
import com.app.bestiepanti.dto.response.donatur.DonaturResponse;
// import com.app.bestiepanti.dto.response.UserResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

    private final UserService userService;
    
    @RequestMapping(value = REGISTER_ENDPOINT, method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<DonaturResponse> register(@Valid @RequestBody RegisterRequest userRequest){        
        DonaturResponse DonaturResponse = userService.register(userRequest);
        return new ResponseEntity<>(DonaturResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = LOGIN_ENDPOINT, method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest userRequest) throws UserNotFoundException{
        Object userResponse = userService.login(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
    
}