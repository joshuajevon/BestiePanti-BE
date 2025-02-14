package com.app.bestiepanti.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LoginRequest {

    @NotEmpty(message = "Email tidak boleh kosong")
    @Email(message = "Email harus valid")
    private String email;

    @NotEmpty(message = "Kata Sandi tidak boleh kosong")
    private String password;
}
