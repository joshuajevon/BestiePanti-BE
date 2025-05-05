package com.app.bestiepanti.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.bestiepanti.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, BigInteger>{
    
    @Query(value = "SELECT * FROM Messages WHERE donatur_id = ?1 ORDER BY id DESC", nativeQuery = true)
    List<Message> findAllByDonaturId(@Param("id") BigInteger id);
    
    @Query(value = "SELECT * FROM Messages WHERE panti_id = ?1 ORDER BY id DESC", nativeQuery = true)
    List<Message> findAllByPantiId(@Param("id") BigInteger id);
}
