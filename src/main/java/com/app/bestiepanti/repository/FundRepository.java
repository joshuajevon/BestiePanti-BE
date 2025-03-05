package com.app.bestiepanti.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bestiepanti.model.Fund;

@Repository
public interface FundRepository extends JpaRepository<Fund, BigInteger>{
    @Query(value = "SELECT * FROM Funds WHERE donation_id = ?1", nativeQuery = true)
    Fund findByDonationId(BigInteger donationId);  

    @Query(value = "DELETE FROM Funds WHERE donation_id = ?1", nativeQuery = true)
    void deleteByDonationId(BigInteger donationId);
}
