package com.app.bestiepanti.controller;

import java.math.BigInteger;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.MessageRequest;
import com.app.bestiepanti.dto.response.MessageResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.service.MessageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/message")
public class MessageController {
    public static final String CREATE_MESSAGE_ENDPOINT = "/create/{pantiId}";

    private final MessageService messageService;

    @RequestMapping(value = CREATE_MESSAGE_ENDPOINT, method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<MessageResponse> createMessage(@Valid @RequestBody MessageRequest request, @PathVariable BigInteger pantiId) throws UserNotFoundException {
        MessageResponse messageResponse = messageService.createMessage(request, pantiId);
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }
    
}
