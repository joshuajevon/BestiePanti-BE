package com.app.bestiepanti.dto.response.message;

import java.math.BigInteger;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MessageResponse {
    private BigInteger id;

    private BigInteger donaturId;

    private String donaturName;

    private BigInteger pantiId;

    private String message;

    private LocalDateTime timestamp;

    private Integer isShown;
}
