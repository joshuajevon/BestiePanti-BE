package com.app.bestiepanti.dto.response.donatur;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DonaturResponses {
    
    @JsonProperty(value = "donatur_responses")
    List<DonaturResponse> donaturResponses;
}
