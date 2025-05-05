package com.app.bestiepanti.dto.response.donation.fund;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FundDonationResponses {
    
    @JsonProperty(value = "fund_donation_responses")
    List<FundDonationResponse> fundDonationResponses;
}
