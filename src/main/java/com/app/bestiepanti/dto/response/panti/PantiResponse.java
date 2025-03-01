package com.app.bestiepanti.dto.response.panti;

import java.math.BigInteger;
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
public class PantiResponse{

    private BigInteger id;
    
    private String name;

    private String email;

    private String role;

    private String token;

    private List<String> image;
    
    private String description;
 
    private String phone;
 
    private List<String> donationTypes;
 
    private Integer isUrgent;
 
    private String address;

    private String region;
 
    private String qris;
}
