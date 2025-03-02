package com.app.bestiepanti.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.bestiepanti.model.Donation;

@Repository
public interface DonationRepository extends JpaRepository<Donation, BigInteger>{
    @Query(value = "SELECT * FROM Donations WHERE donatur_id = ?1", nativeQuery = true)
    List<Donation> findAllByDonaturId(@Param("id") BigInteger id);
    
    @Query(value = "SELECT * FROM Donations WHERE panti_id = ?1", nativeQuery = true)
    List<Donation> findAllByPantiId(@Param("id") BigInteger id);
}
