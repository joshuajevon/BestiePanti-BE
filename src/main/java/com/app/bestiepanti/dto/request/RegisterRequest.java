package com.app.bestiepanti.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RegisterRequest {

    private String name;

    private String email;
    
    private String password;

    private String role;

    private String phone;

    private String gender;

    private String dob;

    private String address;
    
}
