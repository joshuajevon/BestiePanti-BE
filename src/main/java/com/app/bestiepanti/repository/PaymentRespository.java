package com.app.bestiepanti.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bestiepanti.model.Payment;


@Repository
public interface PaymentRespository extends JpaRepository<Payment, BigInteger>{
    @Query(value = "SELECT * FROM Payments WHERE panti_id = ?1", nativeQuery = true)
    Payment findByPantiId(BigInteger pantiId);  

}
