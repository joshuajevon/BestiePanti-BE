package com.app.bestiepanti.dto.response.donatur;

import java.math.BigInteger;

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
public class DonaturResponse{

    private BigInteger id;
    
    private String name;

    private String email;

    private String role;

    private String token;

    private String phone;

    private String gender;

    private String dob;

    private String address;

    private String profile;
}
