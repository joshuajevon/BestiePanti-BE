package com.app.bestiepanti.dto.request.auth.forgotpassword;

import com.app.bestiepanti.validation.general.ConfirmationPassword;
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
public class ChangePasswordRequest {

    @NotEmpty(message = "Email tidak boleh kosong")
    @Email(message = "Email harus valid")
    private String email;

    @NotEmpty(message = "Kata Sandi tidak boleh kosong")
    @Size(min = 8, message = "Kata Sandi harus memiliki minimal 8 karakter")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", message = "Kata Sandi harus mengandung huruf dan angka")
    private String password;

    @NotEmpty(message = "Konfirmasi Kata Sandi tidak boleh kosong")
    private String confirmationPassword;
}