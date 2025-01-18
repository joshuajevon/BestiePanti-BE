package com.app.bestiepanti.dto.response;

import java.math.BigInteger;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserResponse {
    
    private BigInteger id;
    
    private String name;

    private String email;

    private String role;

    private String phone;

    private String gender;

    private String dob;

    private String address;

    private String token;
}
