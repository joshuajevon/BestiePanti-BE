package com.app.bestiepanti.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.auth.LoginRequest;
import com.app.bestiepanti.dto.request.auth.changecredential.ChangeEmailRequest;
import com.app.bestiepanti.dto.request.auth.changecredential.ChangePasswordRequest;
import com.app.bestiepanti.dto.request.auth.forgotpassword.ResetPasswordRequest;
import com.app.bestiepanti.dto.request.auth.forgotpassword.VerifyEmailRequest;
import com.app.bestiepanti.dto.request.auth.forgotpassword.VerifyOtpRequest;
import com.app.bestiepanti.dto.request.auth.register.RegisterRequest;
import com.app.bestiepanti.dto.request.auth.register.VerifyOtpRegisterRequest;
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

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;

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
    public static final String FORGOT_PASSWORD_ENDPOINT = "/forgot-password";
    public static final String VERIFY_EMAIL_ENDPOINT = FORGOT_PASSWORD_ENDPOINT + "/verify-email";
    public static final String VERIFY_OTP_FORGOT_PASSWORD_ENDPOINT = FORGOT_PASSWORD_ENDPOINT + "/verify-otp";
    public static final String RESET_PASSWORD_ENDPOINT = FORGOT_PASSWORD_ENDPOINT + "/reset-password";
    public static final String CHANGE_PASSWORD_ENDPOINT = "/change-password";
    public static final String CHANGE_EMAIL_ENDPOINT = "/change-email";
    public static final String CHECK_EMAIL_ENDPOINT = "/check-email";
    public static final String VERIFY_OTP_REGISTRATION_ENDPOINT = "/verify-otp";
    public static final String LOGIN_GOOGLE_ENDPOINT = LOGIN_ENDPOINT + "/google";

    private final UserService userService;
    private final DonaturService donaturService;
    private final PantiService pantiService;
    
    @RequestMapping(value = REGISTER_ENDPOINT, method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest userRequest) throws Exception{     
        userService.sendOtpTwoStepRegistration(userRequest);
        log.info("Email has been sent to email's donatur: " + userRequest.getEmail());
        GeneralResponse generalResponse = new GeneralResponse("Email sudah terkirim kepada email donatur: " + userRequest.getEmail());
        return new ResponseEntity<>(generalResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = VERIFY_OTP_REGISTRATION_ENDPOINT, method = RequestMethod.POST)
    public ResponseEntity<Object> verifyOtpRegistration(@Valid @RequestBody VerifyOtpRegisterRequest request) throws UserNotFoundException{
        DonaturResponse donaturResponse = userService.verifyOtpRegistration(request);
        log.info("Otp verified for email " + donaturResponse.getEmail() + "!");
        return new ResponseEntity<>(donaturResponse, HttpStatus.OK);
    }

    @RequestMapping(value = LOGIN_ENDPOINT, method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest userRequest) throws UserNotFoundException{
        Object userResponse = userService.login(userRequest,false);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @RequestMapping(value = LOGIN_GOOGLE_ENDPOINT, method = RequestMethod.GET)
    public void authenticateWithGoogle(@AuthenticationPrincipal OAuth2User principal, HttpServletResponse response) throws UserNotFoundException, IOException {
        userService.authenticateWithGoogle(principal, response);
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

    @RequestMapping(value = VERIFY_EMAIL_ENDPOINT, method=RequestMethod.POST)
    public ResponseEntity<Object> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) throws Exception {
        userService.verifyEmail(request.getEmail());
        GeneralResponse generalResponse = new GeneralResponse("Email sudah terkirim ke " + request.getEmail() + "!");
        log.info("Email sent for verification to " + request.getEmail() + "!");
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @RequestMapping(value = VERIFY_OTP_FORGOT_PASSWORD_ENDPOINT, method = RequestMethod.POST)
    public ResponseEntity<Object> verifyOtpForgotPassword(@Valid @RequestBody VerifyOtpRequest request) throws UserNotFoundException{
        userService.verifyOtpForgotPassword(request);
        GeneralResponse generalResponse = new GeneralResponse("Otp sudah terverifikasi untuk " + request.getEmail() + "!");
        log.info("Otp verified for email " + request.getEmail() + "!");
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }
    
    @RequestMapping(value = RESET_PASSWORD_ENDPOINT, method=RequestMethod.POST)
    public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPassword) {
        userService.resetPassword(resetPassword);
        GeneralResponse generalResponse = new GeneralResponse("Password sudah berhasil diperbaharui untuk " + resetPassword.getEmail() + "!");
        log.info("Password has been changed for email " + resetPassword.getEmail() + "!");
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @RequestMapping(value = CHANGE_PASSWORD_ENDPOINT, method=RequestMethod.PATCH)
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordRequest request) throws UserNotFoundException {
        userService.changePassword(request);
        GeneralResponse generalResponse = new GeneralResponse("Password sudah berhasil diperbaharui!");
        log.info("Password has been changed!");
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @RequestMapping(value = CHANGE_EMAIL_ENDPOINT, method=RequestMethod.PATCH)
    public ResponseEntity<Object> changeEmail(@Valid @RequestBody ChangeEmailRequest request) throws Exception {
        userService.changeEmail(request);
        GeneralResponse generalResponse = new GeneralResponse("Email sudah berhasil dikirim ke " + request.getEmail() +"!");
        log.info("Email has been sent to " + request.getEmail() + "!");
        return new ResponseEntity<>(generalResponse, HttpStatus.OK);
    }

    @RequestMapping(value = CHECK_EMAIL_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<Object> checkEmail(@RequestParam("token") String token) throws UserNotFoundException {
        HttpHeaders headers = userService.checkEmail(token);
        log.info("Email has been updated!");
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}