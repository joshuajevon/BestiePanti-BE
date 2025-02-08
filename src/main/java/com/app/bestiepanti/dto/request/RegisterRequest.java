package com.app.bestiepanti.dto.request;

import com.app.bestiepanti.validation.register.ConfirmationPassword;
import com.app.bestiepanti.validation.register.UniqueEmail;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
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
@ConfirmationPassword
public class RegisterRequest {
    @NotEmpty(message = "Name field cannot be empty")
    private String name;
    
    @NotEmpty(message = "Email field cannot be empty")
    @Email(message = "Email should be valid")
    @UniqueEmail(message = "Email already exists")
    private String email;
    
    @NotEmpty(message = "Password field cannot be empty") 
    @Size(min = 6, message = "Password should have at least 6 characters") 
    private String password;
    
    private String confirmationPassword;
    
    @NotEmpty(message = "Phone field cannot be empty") 
    private String phone;
    
    @NotEmpty(message = "Gender field cannot be empty") 
    private String gender;
    
    @NotEmpty(message = "Date of Birth field cannot be empty") 
    private String dob;
    
    @NotEmpty(message = "Address field cannot be empty") 
    private String address;
    
}
