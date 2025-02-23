package me.matule.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.CategoryDto;
import me.matule.backend.data.dto.ProductDto;
import me.matule.backend.service.CategoryService;
import me.matule.backend.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "Категории", description = "Контролер для работы с категориями")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @Operation(
            summary = "Получить все категории товаров",
            description = "Возвращает все категории товаров"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категории успешно получены")
    })
    @GetMapping
    public List<CategoryDto> getAll() {
        return categoryService.getAll();
    }

    @Operation(
            summary = "Получить категорию по id",
            description = "Возвращает категорию товаров по id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категории успешно получены")
    })
    @GetMapping("/{id}")
    public CategoryDto getOne(@PathVariable Long id) {
        return categoryService.getOne(id);
    }

    @Operation(
            summary = "Получить товары по id категории",
            description = "Возвращает товары по id категории"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товары успешно получены")
    })
    @GetMapping("/{id}/products")
    public List<ProductDto> getProductsBy(@PathVariable Long id) {
        return productService.getByCategory(id);
    }
}
