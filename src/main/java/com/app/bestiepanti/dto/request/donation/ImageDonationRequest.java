package com.app.bestiepanti.dto.request.donation;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageDonationRequest {
    List<MultipartFile> getImages();
}
