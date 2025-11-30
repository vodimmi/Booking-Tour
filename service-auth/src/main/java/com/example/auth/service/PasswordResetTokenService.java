package com.example.auth.service;

import com.example.auth.entity.PasswordResetToken;
import com.example.auth.entity.User;
import com.example.auth.repository.PasswordResetTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService {
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetTokenService.class);
    private static final int TOKEN_VALIDITY_MINUTES = 30;

    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public String createPasswordResetToken(User user) {
        // Delete any existing tokens for this user
        tokenRepository.deleteByUser(user);

        // Generate new token
        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plusSeconds(TOKEN_VALIDITY_MINUTES * 60);

        PasswordResetToken resetToken = new PasswordResetToken(user, token, expiresAt);
        tokenRepository.save(resetToken);

        logger.info("Created password reset token for user: {} (expires at: {})", user.getEmail(), expiresAt);
        return token;
    }

    @Transactional
    public Optional<PasswordResetToken> validateAndGetToken(String token) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);

        if (resetToken.isEmpty()) {
            logger.warn("Password reset token not found: {}", token);
            return Optional.empty();
        }

        if (!resetToken.get().isValid()) {
            logger.warn("Password reset token is invalid (expired or used): {}", token);
            return Optional.empty();
        }

        return resetToken;
    }

    @Transactional
    public void markTokenAsUsed(PasswordResetToken token) {
        token.setUsed(true);
        tokenRepository.save(token);
        logger.info("Marked password reset token as used for user: {}", token.getUser().getEmail());
    }

    @Scheduled(cron = "0 0 * * * *") // Run every hour
    @Transactional
    public void cleanupExpiredTokens() {
        tokenRepository.deleteExpiredAndUsedTokens(Instant.now());
        logger.info("Cleaned up expired and used password reset tokens");
    }
}
