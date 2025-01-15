package com.app.bestiepanti.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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

    @Min(value = 2, message = "Name should have at least 2 characters") 
    @NotBlank(message = "Name field cannot be empty")
    private String name;
    
    @NotEmpty(message = "Email field cannot be empty") 
    @Email(message = "Email should be valid") 
    private String email;
    
    @NotEmpty(message = "Password field cannot be empty") 
    @Size(min = 6, message = "Password should have at least 6 characters") 
    private String password;

    private String role;
    
    @NotEmpty(message = "Phone field cannot be empty") 
    private String phone;
    
    @NotEmpty(message = "Gender field cannot be empty") 
    private String gender;
    
    @NotEmpty(message = "Date of Birth field cannot be empty") 
    private String dob;
    
    @NotEmpty(message = "Address field cannot be empty") 
    private String address;
    
}
