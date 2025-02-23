package com.app.bestiepanti.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.bestiepanti.dto.request.MessageRequest;
import com.app.bestiepanti.dto.response.MessageResponse;
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
