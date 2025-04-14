package com.app.bestiepanti.controller;

import java.math.BigInteger;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.bestiepanti.dto.request.MessageRequest;
import com.app.bestiepanti.dto.response.GeneralResponse;
import com.app.bestiepanti.dto.response.message.MessageResponse;
import com.app.bestiepanti.dto.response.message.MessageResponses;
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
    public static final String VIEW_MESSAGE_BY_USER_ID_ENDPOINT = "/view/{userId}";
    public static final String VIEW_ALL_MESSAGES_ENDPOINT = "/view";
    public static final String ACCEPT_MESSAGE_ENDPOINT = "/accept/{id}";
    public static final String DELETE_MESSAGE_ENDPOINT = "/delete/{id}";

    private final MessageService messageService;

    @RequestMapping(value = CREATE_MESSAGE_ENDPOINT, method=RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<MessageResponse> createMessage(@Valid @RequestBody MessageRequest request, @PathVariable BigInteger pantiId) throws Exception {
        log.info("Request Body: " + request);
        MessageResponse messageResponse = messageService.createMessage(request, pantiId);
        log.info("Donatur " + messageResponse.getDonaturId() + " create message for panti " + pantiId + "and email has been sent!");
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }

    @RequestMapping(value = VIEW_ALL_MESSAGES_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<MessageResponses> viewAllMessages() {
        MessageResponses messageResponses = new MessageResponses();
        List<MessageResponse> messageResponseList = messageService.viewAllMessages();
        messageResponses.setMessageResponses(messageResponseList);
        log.info("Response Body: " + messageResponses);
        return new ResponseEntity<>(messageResponses, HttpStatus.OK);
    }

    @RequestMapping(value = VIEW_MESSAGE_BY_USER_ID_ENDPOINT, method=RequestMethod.GET)
    public ResponseEntity<MessageResponses> viewMessageByUserId(@PathVariable BigInteger userId) throws UserNotFoundException {
        MessageResponses messageResponses = new MessageResponses();
        List<MessageResponse> messageResponseList = messageService.viewMessageByUserId(userId);
        messageResponses.setMessageResponses(messageResponseList);
        log.info("Response Body: " + messageResponses);
        return new ResponseEntity<>(messageResponses, HttpStatus.OK);
    }
    
    @RequestMapping(value = ACCEPT_MESSAGE_ENDPOINT, method=RequestMethod.PUT)
    public ResponseEntity<GeneralResponse> acceptMessage(@PathVariable BigInteger id) throws UserNotFoundException {
        try {
            messageService.acceptMessage(id);
            GeneralResponse generalResponse = new GeneralResponse("Message with ID " + id + " has been accepted!");
            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            GeneralResponse generalResponse = new GeneralResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generalResponse);
        }
    }

    @RequestMapping(value = DELETE_MESSAGE_ENDPOINT, method=RequestMethod.DELETE)
    public ResponseEntity<GeneralResponse> deleteMessage(@PathVariable BigInteger id) throws UserNotFoundException {
        try {
            messageService.deleteMessage(id);
            GeneralResponse generalResponse = new GeneralResponse("Message with ID " + id + " has been deleted!");
            return new ResponseEntity<>(generalResponse, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            GeneralResponse generalResponse = new GeneralResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generalResponse);
        }
    }
    
    
}
