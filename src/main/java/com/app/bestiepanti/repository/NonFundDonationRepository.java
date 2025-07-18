package com.app.bestiepanti.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bestiepanti.model.NonFund;

@Repository
public interface NonFundDonationRepository extends JpaRepository<NonFund, BigInteger>{
    @Query(value = "SELECT * FROM NonFunds WHERE donation_id = ?1", nativeQuery = true)
    NonFund findByDonationId(BigInteger donationId);  

}
