package com.app.bestiepanti.dto.request.donation.nonfund;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateNonFundDonationRequest {
    @NotEmpty(message = "Tanggal donasi tidak boleh kosong")
    private String donationDate;

    @NotEmpty(message = "Kehadiran tidak boleh kosong")
    private String isOnsite;

    @NotEmpty(message = "Jenis Donasi tidak boleh kosong")
    private List<String> donationTypes;
    
    @NotEmpty(message = "Penanggung jawab tidak boleh kosong")
    private String pic;
    
    @NotEmpty(message = "Nomor aktif tidak boleh kosong")
    private String activePhone;

    @NotEmpty(message = "Catatan tidak boleh kosong")
    private String notes;

    @NotEmpty(message = "Status tidak boleh kosong")
    private String status;
}
