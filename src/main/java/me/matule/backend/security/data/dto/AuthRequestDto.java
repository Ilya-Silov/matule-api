package me.matule.backend.security.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "Запрос на аутентификацию пользователя")
public record AuthRequestDto(
        @Schema(description = "Email пользователя", example = "user@example.com", required = true)
        String email,

        @Schema(description = "Пароль пользователя", example = "securePassword123", required = true)
        String password
) {}
