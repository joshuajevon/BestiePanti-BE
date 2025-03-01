package com.app.bestiepanti.dto.request.panti;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

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
public class UpdatePantiRequest implements ImageRequest{
    
    @NotEmpty(message = "Nama tidak boleh kosong")
    private String name;

    @NotEmpty(message = "Email tidak boleh kosong")
    @Email(message = "Email harus valid")
    private String email;

    @NotEmpty(message = "Gambar tidak boleh kosong")
    private List<MultipartFile> image;

    @NotEmpty(message = "Deskripsi tidak boleh kosong")
    private String description;

    @NotEmpty(message = "Nomor Telepon tidak boleh kosong")
    private String phone;

    @NotEmpty(message = "Jenis Donasi tidak boleh kosong")
    private List<String> donationTypes;

    @NotNull(message = "Urgensi tidak boleh kosong")
    private String isUrgent;

    @NotEmpty(message = "Alamat tidak boleh kosong")
    private String address;

    @NotEmpty(message = "Wilayah tidak boleh kosong")
    private String region;
   
    private MultipartFile qris;
}
