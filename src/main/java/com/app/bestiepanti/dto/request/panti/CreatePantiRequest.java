package com.app.bestiepanti.dto.request.panti;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.app.bestiepanti.validation.general.ConfirmationPassword;
import com.app.bestiepanti.validation.general.UniqueEmail;
import com.app.bestiepanti.validation.image.FileType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ConfirmationPassword
public class CreatePantiRequest implements ImagePantiRequest{
    
    @NotEmpty(message = "Nama tidak boleh kosong")
    private String name;

    @NotEmpty(message = "Email tidak boleh kosong")
    @Email(message = "Email harus valid")
    @UniqueEmail(message = "Email sudah terdaftar")
    private String email;

    @NotEmpty(message = "Kata Sandi tidak boleh kosong")
    @Size(min = 8, message = "Kata Sandi harus memiliki minimal 8 karakter")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d!@#$%^&*()_+\\-={}\\[\\]:\";'<>?,./|\\\\`~]+$", message = "Kata Sandi harus mengandung huruf dan angka")
    private String password;

    private String confirmationPassword;

    @NotEmpty(message = "Gambar tidak boleh kosong")
    @FileType(allowedTypes = {"image/png", "image/jpeg","image/jpg"}, message = "File harus format PNG / JPEG / JPG")
    private List<MultipartFile> image;

    @NotEmpty(message = "Deskripsi tidak boleh kosong")
    private String description;

    @NotEmpty(message = "Nomor Telepon tidak boleh kosong")
    @Pattern(regexp = "8\\d+", message = "Nomor Telepon harus numerik dan dimulai dengan angka 8")
    private String phone;

    @NotEmpty(message = "Jenis Donasi tidak boleh kosong")
    private List<String> donationTypes;

    @NotNull(message = "Urgensi tidak boleh kosong")
    private String isUrgent;

    @NotEmpty(message = "Alamat tidak boleh kosong")
    private String address;

    @NotEmpty(message = "Wilayah tidak boleh kosong")
    private String region;

    @NotEmpty(message = "Google Maps tidak boleh kosong")
    private String maps;

    @NotEmpty(message = "Nama bank tidak boleh kosong")
    private String bankName;

    @NotEmpty(message = "Nomor rekening tidak boleh kosong")
    @Pattern(regexp = "\\d+", message = "Nomor rekening harus numerik")
    private String bankAccountNumber;
    
    @NotEmpty(message = "Nama pemilik rekening tidak boleh kosong")
    private String bankAccountName;
   
    @FileType(allowedTypes = {"image/png", "image/jpeg","image/jpg"}, message = "File harus format PNG / JPEG / JPG")
    private MultipartFile qris;
}
