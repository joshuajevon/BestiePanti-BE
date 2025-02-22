package com.app.bestiepanti.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.bestiepanti.model.Panti;

@Repository
public interface PantiRepository extends JpaRepository<Panti, BigInteger>{
   Panti findByUserId(BigInteger userId);
   void deleteByUserId(BigInteger userId);
   List<Panti> findAllByIsUrgent(Integer isUrgent);
}
