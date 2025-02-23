package me.matule.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.PromotionDto;
import me.matule.backend.service.PromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
@Tag(name = "Акции", description = "API для управления акциями")
public class PromotionResource {

    private final PromotionService promotionService;

    @GetMapping
    @Operation(summary = "Получить список акций", description = "Возвращает список всех доступных акций")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное получение списка"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<PromotionDto>> getAll() {
        List<PromotionDto> promotions = promotionService.getAll();
        return ResponseEntity.ok(promotions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить одну акцию", description = "Возвращает акцию по ее идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное получение акции"),
            @ApiResponse(responseCode = "404", description = "Акция не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<PromotionDto> getOne(
            @PathVariable @Parameter(description = "Идентификатор акции", example = "1") Long id) {
        return ResponseEntity.ok(promotionService.getOne(id));
    }
}