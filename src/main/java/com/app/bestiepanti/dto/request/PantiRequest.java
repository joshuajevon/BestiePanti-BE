package com.app.bestiepanti.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.bestiepanti.validation.general.ConfirmationPassword;
import com.app.bestiepanti.validation.general.UniqueEmail;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class PantiRequest {
    
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
 
    @NotEmpty(message = "Image field cannot be empty")
    private List<MultipartFile> image;

    @NotEmpty(message = "Description field cannot be empty")
    private String description;
 
    @NotEmpty(message = "Phone field cannot be empty")
    private String phone;
   
    @NotEmpty(message = "Donation Types field cannot be empty")
    private List<String> donationTypes;
   
    @NotNull(message = "isUrgent field cannot be empty")
    private String isUrgent;
   
    @NotEmpty(message = "Address field cannot be empty")
    private String address;
   
    private MultipartFile qris;
}
