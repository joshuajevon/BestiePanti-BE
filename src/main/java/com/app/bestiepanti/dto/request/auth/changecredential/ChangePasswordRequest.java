package com.app.bestiepanti.dto.request.auth.changecredential;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
public class ChangePasswordRequest {

    @NotEmpty(message = "Kata Sandi Lama tidak boleh kosong")
    private String currentPassword;

    @NotEmpty(message = "Kata Sandi Baru tidak boleh kosong")
    @Size(min = 8, message = "Kata Sandi Baru harus memiliki minimal 8 karakter")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$", message = "Kata Sandi Baru harus mengandung huruf dan angka")
    private String newPassword;

    @NotEmpty(message = "Konfirmasi Kata Sandi tidak boleh kosong")
    private String confirmationPassword;
}
