package com.app.bestiepanti.dto.request.panti;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ImageRequest {
    List<MultipartFile> getImage();
    MultipartFile getQris();
    String getName();
}
