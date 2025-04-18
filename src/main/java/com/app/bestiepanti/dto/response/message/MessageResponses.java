package com.app.bestiepanti.dto.response.message;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MessageResponses {
    
    @JsonProperty(value = "message_responses")
    List<MessageResponse> messageResponses;
}
