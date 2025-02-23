package me.matule.backend.security.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на отправку кода для сброса пароля")
public record PasswordResetRequestDto(
        @Schema(description = "Email пользователя", example = "user@example.com", required = true)
        String email
) {}
