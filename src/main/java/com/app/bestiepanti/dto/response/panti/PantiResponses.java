package com.app.bestiepanti.dto.response.panti;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PantiResponses {
    
    @JsonProperty(value = "panti_responses")
    List<PantiResponse> pantiResponses;
}
