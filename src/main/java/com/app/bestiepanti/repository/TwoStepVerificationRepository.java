package com.app.bestiepanti.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.bestiepanti.model.TwoStepVerification;

public interface TwoStepVerificationRepository extends JpaRepository<TwoStepVerification, BigInteger>{
    TwoStepVerification findByEmail(String email);
    void deleteByEmail(String email);
}
