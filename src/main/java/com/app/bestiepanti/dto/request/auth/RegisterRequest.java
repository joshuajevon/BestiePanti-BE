package com.app.bestiepanti.dto.request.auth;

import com.app.bestiepanti.validation.general.ConfirmationPassword;
import com.app.bestiepanti.validation.general.UniqueEmail;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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
public class RegisterRequest {
    @NotEmpty(message = "Nama tidak boleh kosong")
    private String name;

    @NotEmpty(message = "Email tidak boleh kosong")
    @Email(message = "Email harus valid")
    @UniqueEmail(message = "Email sudah terdaftar")
    private String email;

    @NotEmpty(message = "Kata Sandi tidak boleh kosong")
    @Size(min = 8, message = "Kata Sandi harus memiliki minimal 8 karakter")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", message = "Kata Sandi harus mengandung huruf dan angka")
    private String password;

    private String confirmationPassword;

    @NotEmpty(message = "Nomor Telepon tidak boleh kosong")
    private String phone;

    @NotEmpty(message = "Jenis Kelamin tidak boleh kosong")
    private String gender;

    @NotEmpty(message = "Tanggal Lahir tidak boleh kosong")
    private String dob;

    @NotEmpty(message = "Alamat tidak boleh kosong")
    private String address;
    
}
