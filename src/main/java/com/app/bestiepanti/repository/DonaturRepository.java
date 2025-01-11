package com.app.bestiepanti.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.bestiepanti.model.Donatur;

public interface DonaturRepository extends JpaRepository<Donatur, BigInteger> {
    Donatur findByUserId(BigInteger userId);
}
