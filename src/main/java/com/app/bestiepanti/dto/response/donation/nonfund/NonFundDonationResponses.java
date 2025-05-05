package com.app.bestiepanti.dto.response.donation.nonfund;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class NonFundDonationResponses {
    @JsonProperty(value = "nonfund_donation_responses")
    List<NonFundDonationResponse> nonFundDonationResponse;
}
