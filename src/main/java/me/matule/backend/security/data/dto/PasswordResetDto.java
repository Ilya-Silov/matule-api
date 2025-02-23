package me.matule.backend.security.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на смену пароля")
public record PasswordResetDto(
        @Schema(description = "6-значный код восстановления пароля", example = "123456", required = true)
        String token,

        @Schema(description = "Новый пароль", example = "newSecurePassword123", required = true)
        String newPassword
) {}