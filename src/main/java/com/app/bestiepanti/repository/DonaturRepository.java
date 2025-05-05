package com.app.bestiepanti.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.bestiepanti.model.Donatur;

@Repository
public interface DonaturRepository extends JpaRepository<Donatur, BigInteger> {
    List<Donatur> findAllByOrderByIdDesc();
    Donatur findByUserId(BigInteger userId);
    void deleteByUserId(BigInteger userId);
}
