package com.app.bestiepanti.repository;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.bestiepanti.model.ForgotPassword;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer>{
    Optional<ForgotPassword> findByOtpAndUserId(Integer otp, BigInteger userId);
}
