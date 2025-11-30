package com.example.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    /**
     * Mock email service - logs the reset password link
     * In production, integrate with actual email service (SendGrid, AWS SES, etc.)
     */
    public void sendPasswordResetEmail(String email, String resetToken) {
        String resetLink = String.format("http://localhost:6060/reset-password?token=%s", resetToken);

        logger.info("===========================================");
        logger.info("📧 MOCK EMAIL - Password Reset");
        logger.info("To: {}", email);
        logger.info("Subject: Reset Your Password");
        logger.info("Reset Link: {}", resetLink);
        logger.info("This link will expire in 30 minutes");
        logger.info("===========================================");

        // TODO: Integrate with real email service
        // Example:
        // emailClient.send(email, "Reset Your Password", resetLink);
    }

    public void sendPasswordChangedNotification(String email) {
        logger.info("===========================================");
        logger.info("📧 MOCK EMAIL - Password Changed");
        logger.info("To: {}", email);
        logger.info("Subject: Password Changed Successfully");
        logger.info("Your password has been changed successfully.");
        logger.info("If you did not make this change, please contact support immediately.");
        logger.info("===========================================");
    }
}
