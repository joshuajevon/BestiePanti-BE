package com.app.bestiepanti.dto.response.donation;

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
public class DonationResponse {

    private BigInteger id;

    private BigInteger donaturId;

    private BigInteger pantiId;

    private String donationDate;

    private Integer isOnsite;

    private List<String> donationTypes;
    
    private List<String> images;

    private String notes;

    private String number;

    private String status;

    private LocalDateTime insertedTimestamp;

    private LocalDateTime verifiedTimestamp;
}
