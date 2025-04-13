package com.app.bestiepanti.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.app.bestiepanti.dto.request.MessageRequest;
import com.app.bestiepanti.dto.request.auth.MailRequest;
import com.app.bestiepanti.dto.response.message.MessageResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.Donatur;
import com.app.bestiepanti.model.Message;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.DonaturRepository;
import com.app.bestiepanti.repository.MessageRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final DonaturRepository donaturRepository;
    private final EmailService emailService;

    public MessageResponse createMessage(MessageRequest request, BigInteger pantiId) throws Exception {
        UserApp userDonatur = userService.getAuthenticate();
        UserApp userPanti = userRepository.findById(pantiId).orElseThrow(() -> new UserNotFoundException("User with id " + pantiId + " Not Found"));
        Message message = new Message();
        Donatur donatur = donaturRepository.findByUserId(userDonatur.getId());

        if(userPanti != null && userDonatur != null){
            message.setDonaturId(userDonatur);
            message.setPantiId(userPanti);
            message.setIsShown(0);
            message.setTimestamp(LocalDateTime.now());
            message.setMessage(request.getMessage());
            messageRepository.save(message);
        }

        sendEmailNotificationToDonatur(userDonatur, message);
        return createMessageResponse(message, donatur);
    }

    private void sendEmailNotificationToDonatur(UserApp userDonatur, Message message) throws Exception {
        MailRequest mailBody = MailRequest.builder()
                                .to(userDonatur.getEmail())
                                .subject("[No Reply] Message Details Bestie Panti")
                                .build();
        Map<String, Object> variables = new HashMap<>();
        variables.put("name", userDonatur.getName());      
        variables.put("message", message.getMessage());
        emailService.sendSuccessMessageDetails(mailBody, variables);
    }

    public List<MessageResponse> viewAllMessages() {
        List<Message> messages = messageRepository.findAll();
        List<MessageResponse> messageResponseList = new ArrayList<>();
        if(!messages.isEmpty()){
            for (Message message : messages) {
                Donatur donatur = donaturRepository.findByUserId(message.getDonaturId().getId());
                MessageResponse messageResponse = createMessageResponse(message, donatur);
                messageResponseList.add(messageResponse);
            }
        }
        return messageResponseList;
    }

    public List<MessageResponse> viewMessageByUserId(BigInteger userId) throws UserNotFoundException {
        UserApp user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " Not Found"));
        List<MessageResponse> messageResponses = new ArrayList<>();
        List<Message> messages = new ArrayList<>();

        if(user.getRole().getName().equals(UserApp.ROLE_DONATUR)){
            messages = messageRepository.findAllByDonaturId(userId);
        } else if (user.getRole().getName().equals(UserApp.ROLE_PANTI)){
            messages = messageRepository.findAllByPantiId(userId);
        }

        if(!messages.isEmpty()){
            for (Message message : messages) {
                Donatur donatur = new Donatur();
                donatur = donaturRepository.findByUserId(message.getDonaturId().getId());
                MessageResponse response = createMessageResponse(message, donatur);
                messageResponses.add(response);
            }
        }
        return messageResponses;
    }

    public void acceptMessage(BigInteger id) throws UserNotFoundException{
        try {
            Message message = messageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Message with id " + id + " Not Found"));
            UserApp userPanti = userService.getAuthenticate();
            if(userPanti.getId() != message.getPantiId().getId()){
                throw new UserNotFoundException("User is not permitted to accept this message");
            }

            if(message != null){
                message.setIsShown(1);
                messageRepository.save(message);
            }
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    public void deleteMessage(BigInteger id) throws UserNotFoundException{
        try {
            Message message = messageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Message with id " + id + " Not Found"));
            UserApp user = userService.getAuthenticate();
            if (user.getRole().getName().equals(UserApp.ROLE_PANTI)) {
                if(user.getId() != message.getPantiId().getId()){
                    throw new UserNotFoundException("User is not permitted to delete this message");
                }
            }

            if(message != null){
                messageRepository.delete(message);
            }
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    public MessageResponse createMessageResponse(Message message, Donatur donatur){
        return MessageResponse.builder()
                .id(message.getId())
                .donaturId(donatur.getUser().getId())
                .donaturName(message.getDonaturId().getName())
                .donaturProfile(donatur.getProfile())
                .pantiId(message.getPantiId().getId())
                .message(message.getMessage())
                .timestamp(message.getTimestamp())
                .isShown(message.getIsShown())
                .build();
    }

}
