package me.matule.backend.security.controller;

import me.matule.backend.security.data.dto.PasswordResetDto;
import me.matule.backend.security.data.dto.PasswordResetRequestDto;
import me.matule.backend.security.data.dto.PasswordResetVerifyDto;
import me.matule.backend.security.service.impl.PasswordResetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;


@RestController
@RequestMapping("/password")
@Tag(name = "Password Reset", description = "API для восстановления пароля")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @Operation(
            summary = "Запрос кода для сброса пароля",
            description = "Инициирует процесс сброса пароля, отправляя код на указанный email"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Код успешно отправлен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class, example = "{\"message\": \"Reset code sent\"}"))),
            @ApiResponse(responseCode = "404", description = "Пользователь с таким email не найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class, example = "{\"error\": \"User not found\"}")))
    })
    @PostMapping("/reset/request")
    public ResponseEntity<Map<String, String>> requestPasswordReset(
            @RequestBody PasswordResetRequestDto request) {
        passwordResetService.requestPasswordReset(request.email());
        return ResponseEntity.ok(Map.of("message", "Reset code sent"));
    }

    @Operation(
            summary = "Проверка кода сброса пароля",
            description = "Проверяет валидность предоставленного 6-значного кода сброса пароля"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Результат проверки кода",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class, example = "{\"valid\": true}"))),
            @ApiResponse(responseCode = "400", description = "Неверный код",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class, example = "{\"error\": \"Invalid token\"}")))
    })
    @PostMapping("/reset/verify")
    public ResponseEntity<Map<String, Boolean>> verifyResetCode(
            @RequestBody PasswordResetVerifyDto request) {
        boolean isValid = passwordResetService.verifyResetCode(request.token());
        return ResponseEntity.ok(Map.of("valid", isValid));
    }

    @Operation(
            summary = "Сброс пароля",
            description = "Устанавливает новый пароль после успешной верификации 6-значного кода"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пароль успешно изменён",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class, example = "{\"message\": \"Password reset successful\"}"))),
            @ApiResponse(responseCode = "400", description = "Неверный или просроченный код",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class, example = "{\"error\": \"Invalid token\"}")))
    })
    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestBody PasswordResetDto request) {
        passwordResetService.resetPassword(request.token(), request.newPassword());
        return ResponseEntity.ok(Map.of("message", "Password reset successful"));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of("error", ex.getMessage()));
    }
}