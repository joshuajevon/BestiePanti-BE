package com.app.bestiepanti.dto.request.donation;

import java.util.List;

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
public class UpdateDonationRequest implements ImageDonationRequest{
    
    @NotEmpty(message = "Tanggal donasi tidak boleh kosong")
    private String donationDate;

    @NotEmpty(message = "Kehadiran tidak boleh kosong")
    private String isOnsite;

    @NotEmpty(message = "Jenis Donasi tidak boleh kosong")
    private List<String> donationTypes;
    
    // @NotEmpty(message = "Gambar tidak boleh kosong")
    private List<MultipartFile> images;

    private String number;

    private String notes;

    private String status;
}
