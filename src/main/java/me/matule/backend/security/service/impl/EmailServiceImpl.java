package me.matule.backend.security.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.matule.backend.security.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    @Override
    public void sendResetPasswordEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setFrom("noreply.matule@hnt8.ru"); // Укажите ваш отправитель
            helper.setSubject("Сброс пароля");
            helper.setText(
                    "<h1>Сброс пароля</h1>" +
                            "<p>Здравствуйте!</p>" +
                            "<p>Вы запросили сброс пароля. Используйте этот токен для завершения процесса:</p>" +
                            "<p><strong>" + token + "</strong></p>" +
                            "<p>Если вы не запрашивали сброс, проигнорируйте это письмо.</p>",
                    true // Указывает, что текст в формате HTML
            );

            mailSender.send(message);
            log.info("Email sent to " + to + " with token: " + token);
        } catch (MessagingException e) {
            throw new RuntimeException("Ошибка при отправке email: " + e.getMessage(), e);
        }
    }
}
