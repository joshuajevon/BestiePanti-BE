package com.app.bestiepanti.dto.request.auth;

import lombok.Builder;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class MailRequest{

    @NotEmpty(message = "Target email tidak boleh kosong")
    private String to;

    @NotEmpty(message = "Subjek tidak boleh kosong")
    private String subject;

    @NotEmpty(message = "Isi pesan tidak boleh kosong")
    private String text;
}
