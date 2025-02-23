#LABEL authors="iliya"

# Этап 1: Сборка приложения
FROM gradle:8.5-jdk21 AS builder

# Рабочая директория
WORKDIR /app

# Копируем файлы Gradle
COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle

# Копируем исходный код
COPY src ./src

# Даем права на выполнение gradlew
RUN chmod +x ./gradlew

# Собираем приложение
RUN ./gradlew build --no-daemon

# Этап 2: Создание финального образа
FROM openjdk:21-jdk-slim

# Рабочая директория
WORKDIR /app

# Копируем только собранный JAR из предыдущего этапа
COPY --from=builder /app/build/libs/*.jar app.jar
COPY data ./data

# Указываем порт
EXPOSE 8080

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]