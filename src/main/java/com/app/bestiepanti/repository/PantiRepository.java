package com.app.bestiepanti.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.bestiepanti.model.Panti;

@Repository
public interface PantiRepository extends JpaRepository<Panti, BigInteger>{
   
}
