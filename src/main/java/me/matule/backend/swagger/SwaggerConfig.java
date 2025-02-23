package me.matule.backend.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("MATULE API").version("1.0")
                        .description(
                                "<h2>Авторизация</h2>\n" +
                                "<p>Для авторизации в API используется JWT-токен, который выдаётся после успешной проверки email и пароля.</p>\n" +
                                "<p><strong>Инструкция по авторизации методом API: </strong><br>\n" +
                                "1) Отправьте запрос на авторизацию по адресу <code>/auth/login</code>:<br>\n" +
                                "   - Метод: POST<br>\n" +
                                "   - Тело запроса: <code>{\"email\": \"user@example.com\", \"password\": \"securePassword123\"}</code><br>\n" +
                                "   - Ответ: <code>\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"</code> (JWT-токен)<br>\n" +
                                "2) Добавьте полученный токен в заголовки запросов к защищённым ресурсам:<br>\n" +
                                "   - Заголовок: <code>Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...</code><br>\n" +
                                "</p>\n" +
                                "<p><strong>Примечания:</strong><br>\n" +
                                "- Email и пароль должны соответствовать зарегистрированному пользователю.<br>\n" +
                                "- JWT-токен имеет срок действия (по умолчанию 10 часов), после чего потребуется повторная авторизация.<br>\n" +
                                "- В случае ошибки (неверный email или пароль) сервер вернёт статус 401 и сообщение: <code>{\"error\": \"Unauthorized\"}</code>.<br>\n" +
                                "- Токен необходим для доступа к защищённым эндпоинтам</p>" +

                                "<h2>Регистрация</h2>\n" +
                                "<p>Для регистрации в API необходимо отправить данные пользователя и получить JWT-токен для последующей авторизации.</p>\n" +
                                "<p><strong>Инструкция по регистрации методом API: </strong><br>\n" +
                                "1) Отправьте запрос на регистрацию по адресу <code>/auth/register</code>:<br>\n" +
                                "   - Метод: POST<br>\n" +
                                "   - Тело запроса: <code>{\"email\": \"user@example.com\", \"password\": \"securePassword123\"}</code><br>\n" +
                                "   - Ответ: <code>{\"message\": \"User registered successfully\"}</code><br>\n" +
                                "2) После успешной регистрации отправьте запрос на авторизацию по адресу <code>/auth/login</code>:<br>\n" +
                                "   - Метод: POST<br>\n" +
                                "   - Тело запроса: <code>{\"email\": \"user@example.com\", \"password\": \"securePassword123\"}</code><br>\n" +
                                "   - Ответ: <code>\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"</code> (JWT-токен)<br>\n" +
                                "3) Добавьте полученный токен в заголовки последующих запросов:<br>\n" +
                                "   - Заголовок: <code>Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...</code><br>\n" +
                                "</p>\n" +
                                "<p><strong>Примечания:</strong><br>\n" +
                                "- Email должен быть уникальным, иначе сервер вернёт ошибку.<br>\n" +
                                "- Пароль должен соответствовать требованиям безопасности (например, минимум 8 символов).<br>\n" +
                                "- В случае ошибки (неверный формат данных, существующий пользователь) сервер вернёт соответствующий статус и сообщение, например: <code>{\"error\": \"User already exists\"}</code>.<br>\n" +
                                "- JWT-токен используется для доступа к защищённым ресурсам API.</p>" +

                                "<h2>Восстановление пароля</h2>\n" +
                                "<p>Для восстановления пароля в API используется 6-значный цифровой код, который отправляется на email пользователя.</p>\n" +
                                "<p><strong>Инструкция по восстановлению пароля методом API: </strong><br>\n" +
                                "1) Отправьте запрос на получение кода восстановления по адресу <code>/password/reset/request</code>:<br>\n" +
                                "   - Метод: POST<br>\n" +
                                "   - Тело запроса: <code>{\"email\": \"user@example.com\"}</code><br>\n" +
                                "   - Ответ: <code>{\"message\": \"Reset code sent\"}</code><br>\n" +
                                "2) Получите 6-значный код из email и проверьте его по адресу <code>/password/reset/verify</code>:<br>\n" +
                                "   - Метод: POST<br>\n" +
                                "   - Тело запроса: <code>{\"token\": \"123456\"}</code><br>\n" +
                                "   - Ответ: <code>{\"valid\": true}</code> (или <code>false</code>, если код неверный или просрочен)<br>\n" +
                                "3) Отправьте запрос на смену пароля по адресу <code>/password/reset</code>:<br>\n" +
                                "   - Метод: POST<br>\n" +
                                "   - Тело запроса: <code>{\"token\": \"123456\", \"newPassword\": \"newSecurePassword123\"}</code><br>\n" +
                                "   - Ответ: <code>{\"message\": \"Password reset successful\"}</code><br>\n" +
                                "4) Если код просрочен (действителен 1 минуту), повторите запрос на получение нового кода с шага 1.</p>\n" +
                                "<p><strong>Примечания:</strong><br>\n" +
                                "- Код состоит из 6 цифр (например, 123456).<br>\n" +
                                "- Код действителен 5 минут с момента генерации.<br>\n" +
                                "- Новый код можно запросить через 1 минуту после предыдущего запроса, при этом старый код будет удалён.<br>\n" +
                                "- В случае ошибки (неверный email, код или слишком частый запрос) сервер вернёт соответствующий статус и сообщение об ошибке, например: <code>{\"error\": \"Please wait 1 minute before requesting a new code\"}</code>.</p>"
                                )
                )
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                );
    }
}