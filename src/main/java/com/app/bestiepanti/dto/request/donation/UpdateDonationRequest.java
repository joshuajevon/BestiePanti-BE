package com.app.bestiepanti.dto.request.donation;

import org.springframework.web.multipart.MultipartFile;

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
public class UpdateDonationRequest implements ImageFundDonationRequest{
    
    @NotEmpty(message = "Nomor rekening tidak boleh kosong")
    private String accountNumber;
    
    @NotEmpty(message = "Nama pemilik rekening tidak boleh kosong")
    private String accountName;
    
    @NotEmpty(message = "Bukti transfer tidak boleh kosong")
    private MultipartFile image;

    @NotEmpty(message = "Jumlah transfer tidak boleh kosong")
    private String nominalAmount;

    @NotEmpty(message = "Status tidak boleh kosong")
    private String status;
}
