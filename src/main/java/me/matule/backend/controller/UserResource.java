package me.matule.backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.UserDto;
import me.matule.backend.data.dto.request.OrderRequest;
import me.matule.backend.data.dto.request.UserCreateRequest;
import me.matule.backend.data.dto.request.UserUpdateRequest;
import me.matule.backend.service.UserService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Контроллер для управления пользователями")
public class UserResource {

    private final UserService userService;

//    @PostMapping
//    @Operation(summary = "Создать пользователя", description = "Создает нового пользователя")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
//            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
//            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
//    })
//    public ResponseEntity<UserDto> create(@RequestBody UserCreateRequest dto) {
//        return ResponseEntity.ok(userService.create(dto));
//    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/")
    @Operation(summary = "Обновить пользователя", description = "Частично обновляет пользователя",security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<UserDto> patch(@RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные пользователя",
            content = @Content(schema = @Schema(implementation = UserUpdateRequest.class))
    ) JsonNode patchNode) throws IOException {
        Long userId = getCurrentUserId();
        if (!userId.equals(userService.getOne(userId).getId()))
        {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(userService.patch(userId, patchNode));
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Получаем имя пользователя (может быть email или ID в JWT)
        return Long.parseLong(username); // Если в JWT у вас хранится ID как строка
    }

}
