package me.matule.backend.security.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на проверку кода сброса пароля")
public record PasswordResetVerifyDto(
        @Schema(description = "6-значный код восстановления пароля", example = "123456", required = true)
        String token
) {}
