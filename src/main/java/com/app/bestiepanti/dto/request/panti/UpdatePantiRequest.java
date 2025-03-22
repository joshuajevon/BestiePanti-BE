package com.app.bestiepanti.dto.request.panti;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.bestiepanti.validation.image.FileType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
public class UpdatePantiRequest implements ImagePantiRequest{
    
    @NotEmpty(message = "Nama tidak boleh kosong")
    private String name;

    @NotEmpty(message = "Gambar tidak boleh kosong")
    @FileType(allowedTypes = {"image/png", "image/jpeg","image/jpg"}, message = "File harus format PNG / JPEG / JPG")
    private List<MultipartFile> image;

    @NotEmpty(message = "Deskripsi tidak boleh kosong")
    private String description;

    @NotEmpty(message = "Nomor Telepon tidak boleh kosong")
    private String phone;

    @NotEmpty(message = "Jenis Donasi tidak boleh kosong")
    private List<String> donationTypes;

    @NotEmpty(message = "Alamat tidak boleh kosong")
    private String address;

    @NotEmpty(message = "Wilayah tidak boleh kosong")
    private String region;

    @NotEmpty(message = "Nama bank tidak boleh kosong")
    private String bankName;

    @NotEmpty(message = "Nomor rekening tidak boleh kosong")
    private String bankAccountNumber;
    
    @NotEmpty(message = "Nama pemilik rekening tidak boleh kosong")
    private String bankAccountName;
   
    @FileType(allowedTypes = {"image/png", "image/jpeg","image/jpg"}, message = "File harus format PNG / JPEG / JPG")
    private MultipartFile qris;
}
