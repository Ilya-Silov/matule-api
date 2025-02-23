package me.matule.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.OrderDto;
import me.matule.backend.data.dto.request.OrderRequest;
import me.matule.backend.security.JwtUtil;
import me.matule.backend.service.OrderService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Заказы", description = "API для управления заказами")
public class OrderResource {

    private final OrderService orderService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @Operation(summary = "Получить список заказов", description = "Возвращает список заказов", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<OrderDto>> getAll() {
        Long userId = getCurrentUserId();
        List<OrderDto> orderDtos = orderService.getAll(userId);
        return ResponseEntity.ok(orderDtos);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @Operation(summary = "Получить заказ", description = "Возвращает заказ по его идентификатору", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное получение заказа"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<OrderDto> getOne(
            @PathVariable @Parameter(description = "Идентификатор заказа", example = "1") Long id) {
        Long userId = getCurrentUserId();
        if (!orderService.hasRights(userId, id))
        {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(orderService.getOne(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @Operation(summary = "Создать заказ", description = "Создает новый заказ", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Заказ успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<OrderDto> create(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные нового заказа",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrderRequest.class))
            ) OrderRequest dto) {
        Long userId = getCurrentUserId();
        return ResponseEntity.status(201).body(orderService.create(dto, userId));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заказ", description = "Удаляет заказ по его идентификатору", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заказ успешно удален"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<OrderDto> delete(
            @PathVariable @Parameter(description = "Идентификатор заказа", example = "1") Long id) {
        Long userId = getCurrentUserId();
        if (!orderService.hasRights(userId, id))
        {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(orderService.delete(id));
    }

    // Получение ID текущего пользователя из SecurityContext (JWT токен)
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Получаем имя пользователя (может быть email или ID в JWT)
        return Long.parseLong(username); // Если в JWT у вас хранится ID как строка
    }
}
