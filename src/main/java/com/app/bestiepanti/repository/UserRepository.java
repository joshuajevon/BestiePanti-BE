package com.app.bestiepanti.repository;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.bestiepanti.model.UserApp;

@Repository
public interface UserRepository extends JpaRepository<UserApp, BigInteger> {
    
    Optional<UserApp> findByEmail(String email);
}
