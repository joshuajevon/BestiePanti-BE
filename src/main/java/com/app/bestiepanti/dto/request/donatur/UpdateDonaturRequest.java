package com.app.bestiepanti.dto.request.donatur;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateDonaturRequest implements ProfileDonaturRequest{
    
    @NotEmpty(message = "Nama tidak boleh kosong")
    private String name;

    @NotEmpty(message = "Email tidak boleh kosong")
    @Email(message = "Email harus valid")
    private String email;

    @NotEmpty(message = "Nomor Telepon tidak boleh kosong")
    private String phone;

    @NotEmpty(message = "Jenis Kelamin tidak boleh kosong")
    private String gender;

    @NotEmpty(message = "Tanggal Lahir tidak boleh kosong")
    private String dob;

    @NotEmpty(message = "Alamat tidak boleh kosong")
    private String address; 

    private MultipartFile profile;
   
}
