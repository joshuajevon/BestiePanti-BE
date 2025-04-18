package com.app.bestiepanti.dto.request.auth.changecredential;

import com.app.bestiepanti.validation.general.UniqueEmail;
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
public class ChangeEmailRequest {
    @NotEmpty(message = "Email tidak boleh kosong")
    @Email(message = "Email harus valid")
    @UniqueEmail(message = "Email sudah terdaftar")
    private String email;
}
