package com.app.bestiepanti.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "two_step_verifications")
public class TwoStepVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    
    @Column(nullable = false)
    private String otp;

    @Column(name = "expiration_time")
    private Date expirationTime;

    @Column(name = "verified_timestamp")
    private LocalDateTime verifiedTimestamp;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserApp user;
}
