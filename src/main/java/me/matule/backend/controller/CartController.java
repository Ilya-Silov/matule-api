package me.matule.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.CartItemDto;
import me.matule.backend.data.dto.request.CartItemRequest;
import me.matule.backend.data.dto.request.CartUpdateRequest;
import me.matule.backend.security.JwtUtil;
import me.matule.backend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Корзина", description = "Контролер для работы с корзиной")
public class CartController {

    private final CartService cartService;

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Получить корзину для текущего пользователя",
            description = "Возвращает корзину текущего пользователя.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Корзина успешно получена"),
            @ApiResponse(responseCode = "404", description = "Корзина не найдена")
    })
    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCartForCurrentUser() {
        Long userId = getCurrentUserId(); // Извлечение ID из JWT токена
        return ResponseEntity.ok(cartService.getByUserId(userId)); // Получаем корзину для текущего пользователя
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Добавить товар в корзину для текущего пользователя",
            description = "Добавляет товар в корзину для текущего пользователя",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Товар успешно добавлен"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @PostMapping
    public CartItemDto create(@RequestBody CartItemRequest request) {
        Long userId = getCurrentUserId();
        return cartService.addToCart(request, userId);
    }

    // Частичное обновление корзины для текущего пользователя
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Частичное обновление корзины для текущего пользователя",
            description = "Частично обновляет корзину текущего пользователя, ID которого извлекается из JWT",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Корзина успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Товар не найден")
    })
    @PatchMapping("/{productId}")
    public ResponseEntity<CartItemDto> patch(@PathVariable Long productId, @RequestBody CartUpdateRequest request) throws IOException {
        Long userId = getCurrentUserId();
        Optional<CartItemDto> cartItemDto = cartService.patch(productId, userId, request);
        // Обновляем корзину текущего пользователя
        return cartItemDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    // Удаление корзины для текущего пользователя
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Очищает корзину для текущего пользователя",
            description = "Очищает корзину текущего пользователя",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Корзина успешно очищена"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @DeleteMapping
    public ResponseEntity<Void> delete() {
        Long userId = getCurrentUserId();
        cartService.clear(userId); // Удаляем корзину текущего пользователя
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Удаляет товар из корзины для текущего пользователя",
            description = "Удаляет товар из корзины текущего пользователя",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден; Товар не найден")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        cartService.delete(productId, userId);
        return ResponseEntity.ok().build();
    }

    // Получение ID текущего пользователя из SecurityContext (JWT токен)
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Получаем имя пользователя (может быть email или ID в JWT)
        return Long.parseLong(username); // Если в JWT у вас хранится ID как строка
    }
}
