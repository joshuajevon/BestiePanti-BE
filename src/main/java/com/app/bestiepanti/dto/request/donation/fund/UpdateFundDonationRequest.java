package com.app.bestiepanti.dto.request.donation.fund;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateFundDonationRequest {
    
    @NotEmpty(message = "Nomor rekening tidak boleh kosong")
    @Pattern(regexp = "\\d+", message = "Nomor rekening harus numerik")
    private String accountNumber;
    
    @NotEmpty(message = "Nama pemilik rekening tidak boleh kosong")
    private String accountName;

    @NotEmpty(message = "Jumlah transfer tidak boleh kosong")
    @Pattern(regexp = "\\d+", message = "Jumlah transfer harus numerik")
    private String nominalAmount;

    @NotEmpty(message = "Status tidak boleh kosong")
    private String status;
}
