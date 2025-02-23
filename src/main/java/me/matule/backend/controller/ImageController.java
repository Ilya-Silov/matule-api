package me.matule.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/images")
@Tag(name = "Image", description = "API для доступа к изображениям товаров")
public class ImageController {

    private static final String IMAGE_DIR = "./data/images/";

    @Operation(
            summary = "Получение изображения по ID",
            description = "Возвращает изображение товара по его уникальному идентификатору"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Изображение успешно найдено",
                    content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE)
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Изображение не найдено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @GetMapping("/{imageId}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageId) {
        // Ищем файл, начинающийся с imageId
        File dir = new File(IMAGE_DIR);
        File imageFile = null;
        for (File file : dir.listFiles()) {
            if (file.getName().startsWith(imageId)) {
                imageFile = file;
                break;
            }
        }

        if (imageFile == null || !imageFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(imageFile);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Предполагаем JPEG, можно динамически определять
                .body(resource);
    }
}