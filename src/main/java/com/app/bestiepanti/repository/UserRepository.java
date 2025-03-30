package com.app.bestiepanti.repository;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.bestiepanti.model.UserApp;

import jakarta.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserApp, BigInteger> {
    
    Optional<UserApp> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Users  set password = ?2 where email = ?1", nativeQuery = true)
    void updatePassword(String email, String password);
}
