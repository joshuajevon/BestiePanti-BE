package com.app.bestiepanti.dto.response;

import java.math.BigInteger;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserResponse {
    
    private BigInteger id;
    
    private String name;

    private String email;

    private String role;

}
