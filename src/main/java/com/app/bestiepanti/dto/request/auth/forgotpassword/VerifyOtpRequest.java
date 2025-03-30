package com.app.bestiepanti.dto.request.auth.forgotpassword;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VerifyOtpRequest {
    @NotEmpty(message = "Email tidak boleh kosong")
    @Email(message = "Email harus valid")
    private String email;

    @NotNull(message = "Kode OTP tidak boleh kosong")
    private Integer otp;
}
