package com.app.bestiepanti.repository;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.bestiepanti.model.UserApp;

public interface UserRepository extends JpaRepository<UserApp, BigInteger> {
    
    Optional<UserApp> findByEmail(String email);
}
