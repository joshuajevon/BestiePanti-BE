package com.app.bestiepanti.repository;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.bestiepanti.model.EmailVerification;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, BigInteger>{
    Optional<EmailVerification> findByToken(String token);
}
