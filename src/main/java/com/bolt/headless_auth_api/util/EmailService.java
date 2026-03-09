package com.bolt.headless_auth_api.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${api.security.otp.from-email}")
    private String fromEmail;

    @Value("${api.security.otp.subject}")
    private String subject;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        // In a future phase, will upgrade this to a beautiful HTML template.
        // For V1, plain text is the fastest way to production.
        message.setText("Your authentication code is: " + otp +
                "\n\nThis code will expire in 8 minutes. " +
                "If you did not request this, please ignore this email.");
        mailSender.send(message);
    }
}
