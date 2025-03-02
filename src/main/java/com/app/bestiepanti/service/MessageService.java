package com.app.bestiepanti.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.bestiepanti.dto.request.MessageRequest;
import com.app.bestiepanti.dto.response.message.MessageResponse;
import com.app.bestiepanti.exception.UserNotFoundException;
import com.app.bestiepanti.model.Message;
import com.app.bestiepanti.model.UserApp;
import com.app.bestiepanti.repository.MessageRepository;
import com.app.bestiepanti.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public MessageResponse createMessage(MessageRequest request, BigInteger pantiId) throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserApp userPanti = userRepository.findById(pantiId).orElseThrow(() -> new UserNotFoundException("User with id " + pantiId + " Not Found"));
        String email = authentication.getName();
        UserApp userDonatur = userService.findUserByEmail(email);
        Message message = new Message();

        if(userPanti != null && userDonatur != null){
            message.setDonaturId(userDonatur);
            message.setPantiId(userPanti);
            message.setIsShown(0);
            message.setTimestamp(LocalDateTime.now());
            message.setMessage(request.getMessage());
            messageRepository.save(message);
        }
        return createMessageResponse(message);
    }

    public List<MessageResponse> viewAllMessages() {
        List<Message> messages = messageRepository.findAll();
        List<MessageResponse> messageResponseList = new ArrayList<>();
        if(!messages.isEmpty()){
            for (Message message : messages) {
                MessageResponse messageResponse = createMessageResponse(message);
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
                MessageResponse response = createMessageResponse(message);
                messageResponses.add(response);
            }
        }
        return messageResponses;
    }

    public void acceptMessage(BigInteger id){
        try {
            Message message = messageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Message with id " + id + " Not Found"));
            if(message != null){
                message.setIsShown(1);
                messageRepository.save(message);
            }
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    public void deleteMessage(BigInteger id){
        try {
            Message message = messageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Message with id " + id + " Not Found"));
            if(message != null){
                messageRepository.delete(message);
            }
        } catch (NoSuchElementException e) {
            throw e;
        }
    }

    public MessageResponse createMessageResponse(Message message){
        return MessageResponse.builder()
                .id(message.getId())
                .donaturId(message.getDonaturId().getId())
                .pantiId(message.getPantiId().getId())
                .message(message.getMessage())
                .timestamp(message.getTimestamp())
                .isShown(message.getIsShown())
                .build();
    }

}
