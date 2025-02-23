package me.matule.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.FavoriteDto;
import me.matule.backend.data.dto.request.FavoriteItemRequest;
import me.matule.backend.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

//TODO:TUT XREN
@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
@Tag(name = "Избранное", description = "Контролер для работы с избранным")
public class FavoriteResource {

    private final FavoriteService favoriteService;

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Получить список избранных товаров",
            description = "Возвращает список избранных товаров для текущего пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список избранных товаров успешно получен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping
    public ResponseEntity<List<FavoriteDto>> getAll() {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(favoriteService.getAll(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Добавить товар в список избранных товаров",
            description = "Добавляет товар в список список избранных товаров для текущего пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар успешно добавлен"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PostMapping
    public ResponseEntity<FavoriteDto> create(@RequestBody FavoriteItemRequest dto) {
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(favoriteService.create(userId, dto));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Удалить товар из избранного",
            description = "Method… для текущего пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь/товар не найден")
    })
    @DeleteMapping("/{product_id}")
    public ResponseEntity<?> delete(@PathVariable Long product_id) {
        Long userId = getCurrentUserId();
        favoriteService.delete(product_id, userId);
        return ResponseEntity.noContent().build();
    }

    // Получение ID текущего пользователя из SecurityContext (JWT токен)
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Получаем имя пользователя (может быть email или ID в JWT)
        return Long.parseLong(username); // Если в JWT у вас хранится ID как строка
    }
}
