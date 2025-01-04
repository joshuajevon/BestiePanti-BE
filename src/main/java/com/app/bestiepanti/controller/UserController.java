package com.app.bestiepanti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.UserRequest;
import com.app.bestiepanti.dto.response.UserResponse;
import com.app.bestiepanti.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
@Slf4j
public class UserController {
    
    public static final String REGISTER_ENDPOINT = "/register";

    @Autowired
    private UserService userService;
    
    @RequestMapping(value = REGISTER_ENDPOINT, method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    // ( consumes = "application/json")
    @ResponseBody
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest){
        UserResponse userResponse = userService.createUser(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
    
}