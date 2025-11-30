package com.example.auth.repository;

import com.example.auth.entity.PasswordResetToken;
import com.example.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM PasswordResetToken p WHERE p.user = :user")
    void deleteByUser(@Param("user") User user);

    @Modifying
    @Query("DELETE FROM PasswordResetToken p WHERE p.expiresAt < :now OR p.used = true")
    void deleteExpiredAndUsedTokens(@Param("now") Instant now);
}
