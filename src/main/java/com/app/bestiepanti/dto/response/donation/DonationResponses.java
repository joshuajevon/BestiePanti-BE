package com.app.bestiepanti.dto.response.donation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DonationResponses {
    
    @JsonProperty(value = "donation_responses")
    List<DonationResponse> donationResponses;
}
