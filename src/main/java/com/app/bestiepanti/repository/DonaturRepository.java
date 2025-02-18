package com.app.bestiepanti.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.bestiepanti.model.Donatur;

@Repository
public interface DonaturRepository extends JpaRepository<Donatur, BigInteger> {
    Donatur findByUserId(BigInteger userId);
    void deleteByUserId(BigInteger userId);
}
