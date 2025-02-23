package me.matule.backend.security.service;

import org.springframework.stereotype.Service;

public interface EmailService {
    void sendResetPasswordEmail(String to, String token);
}

