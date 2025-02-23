package me.matule.backend.security.service.impl;

import lombok.RequiredArgsConstructor;
import me.matule.backend.data.entity.User;
import me.matule.backend.repository.UserRepository;
import me.matule.backend.security.data.PasswordResetToken;
import me.matule.backend.security.repository.PasswordResetTokenRepository;
import me.matule.backend.security.service.EmailService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        PasswordResetToken existingToken = tokenRepository.findByUser(user).orElse(null);
        if (existingToken != null && !existingToken.canRequestNew()) {
            throw new IllegalStateException("Please wait 1 minute before requesting a new code");
        }

        if (existingToken != null) {
            // Удаляем старый токен, если он существует
            tokenRepository.delete(existingToken);
        }

        // Генерируем новый 6-значный код
        String token = generateSixDigitCode();
        PasswordResetToken resetToken = new PasswordResetToken(user, token);
        tokenRepository.save(resetToken);

        // Отправляем email с кодом
        emailService.sendResetPasswordEmail(user.getEmail(), token);
    }

    public boolean verifyResetCode(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            return false;
        }
        return true;
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.isExpired()) {
            tokenRepository.delete(resetToken);
            throw new IllegalArgumentException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Удаляем использованный токен
        tokenRepository.delete(resetToken);
    }

    private String generateSixDigitCode() {
        SecureRandom random = new SecureRandom();
        int code = 100000 + random.nextInt(900000); // Генерирует число от 100000 до 999999
        return String.valueOf(code);
    }
}