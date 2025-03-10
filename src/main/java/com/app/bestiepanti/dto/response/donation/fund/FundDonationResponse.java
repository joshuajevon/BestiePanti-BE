package com.app.bestiepanti.dto.response.donation.fund;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FundDonationResponse {

    private BigInteger id;

    private BigInteger donaturId;

    private String donaturName;

    private BigInteger pantiId;

    private String pantiName;

    private String donationDate;

    private Integer isOnsite;

    private List<String> donationTypes;
    
    private String image;

    private String nominalAmount;

    private String accountNumber;
    
    private String accountName;

    private String status;

    private LocalDateTime insertedTimestamp;

    private LocalDateTime verifiedTimestamp;
}
