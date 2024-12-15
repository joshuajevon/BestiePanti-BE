package com.app.bestiepanti.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.service.UserService;

@RestController
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping(value = "/register", consumes = "application/json")
    public UserApp createUser(@RequestBody UserApp user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userService.createUser(user);
    }
    
}