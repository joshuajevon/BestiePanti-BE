package com.app.bestiepanti.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.bestiepanti.model.Donation;

@Repository
public interface DonationRepository extends JpaRepository<Donation, BigInteger>{
    
}
