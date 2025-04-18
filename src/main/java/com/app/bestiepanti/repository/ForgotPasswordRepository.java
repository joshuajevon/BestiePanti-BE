package com.app.bestiepanti.repository;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.bestiepanti.model.ForgotPassword;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer>{
    Optional<ForgotPassword> findTopByUserIdOrderByIdDesc(BigInteger userId);

    @Query(value = "SELECT * FROM Forgot_Passwords fp WHERE fp.user_id = (SELECT u.id FROM Users u WHERE u.email = ?1) ORDER BY fp.id DESC LIMIT 1", nativeQuery = true)
    Optional<ForgotPassword> findTopByUserEmail(String email);
}
