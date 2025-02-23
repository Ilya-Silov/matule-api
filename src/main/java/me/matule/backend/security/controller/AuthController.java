package me.matule.backend.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.matule.backend.data.dto.request.UserCreateRequest;
import me.matule.backend.data.entity.User;
import me.matule.backend.repository.UserRepository;
import me.matule.backend.security.JwtUtil;
import me.matule.backend.security.data.dto.AuthRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API для аутентификации пользователей")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Аутентифицирует пользователя по email и паролю, возвращает JWT токен"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешная аутентификация, возвращён JWT токен",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = String.class, example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Invalid credentials");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        final String jwt = jwtUtil.generateToken(userDetails, request.email());

        return ResponseEntity.ok(jwt);
    }

    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Регистрирует нового пользователя и возвращает сообщение об успехе"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно зарегистрирован",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class, example = "{\"message\": \"User registered successfully\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Пользователь с таким email уже существует",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class, example = "{\"error\": \"User already exists\"}")
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserCreateRequest request) {
        // Проверка на существование пользователя
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Пользователь с таким email уже существует");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Создание нового пользователя
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Предполагаем, что ID генерируется автоматически в базе данных

        // Сохранение пользователя
        User savedUser = userRepository.save(user);

        // Генерация JWT токена
        String token = jwtUtil.generateToken(savedUser, savedUser.getEmail());

        // Формирование ответа
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "Регистрация успешна");

        return ResponseEntity.ok(response);
    }
}
