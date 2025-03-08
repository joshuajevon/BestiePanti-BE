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
    @Query(value = "SELECT * FROM Donations WHERE donatur_id = ?1 AND 'Dana' = ANY (donation_types)", nativeQuery = true)
    List<Donation> findAllByDonaturIdAndFundTypes(@Param("id") BigInteger id);
    
    @Query(value = "SELECT * FROM Donations WHERE panti_id = ?1 AND 'Dana' = ANY (donation_types)", nativeQuery = true)
    List<Donation> findAllByPantiIdAndFundTypes(@Param("id") BigInteger id);

    @Query(value = "SELECT * FROM Donations WHERE donatur_id = ?1 AND NOT ('Dana' = ANY (donation_types))", nativeQuery = true)
    List<Donation> findAllByDonaturIdAndNonFundTypes(@Param("id") BigInteger id);
    
    @Query(value = "SELECT * FROM Donations WHERE panti_id = ?1 AND NOT ('Dana' = ANY (donation_types))", nativeQuery = true)
    List<Donation> findAllByPantiIdAndNonFundTypes(@Param("id") BigInteger id);    
    
    @Query(value = "SELECT * FROM Donations WHERE 'Dana' = ANY (donation_types)", nativeQuery = true)
    List<Donation> findAllByFundTypes();

    @Query(value = "SELECT * FROM Donations WHERE NOT ('Dana' = ANY (donation_types))", nativeQuery = true)
    List<Donation> findAllByNonFundTypes();

    @Query(value = "SELECT * FROM Donations WHERE donatur_id = ?1", nativeQuery = true)
    List<Donation> findAllByDonaturId(@Param("id") BigInteger id);

    @Query(value = "SELECT * FROM Donations WHERE panti_id = ?1", nativeQuery = true)
    List<Donation> findAllByPantiId(@Param("id") BigInteger id);
}
