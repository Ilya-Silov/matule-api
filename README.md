# Развёртывание Spring Boot проекта

## 1. Клонирование репозитория

Перед началом работы склонируйте репозиторий:

```sh
git clone https://github.com/your-repo/your-project.git
cd your-project
```
---
## 2.1. Развёртывание без Docker

### Требования

- Java 21+
- Gradle 8+

### Запуск

1. Соберите проект:
   ```sh
   ./gradlew clean build
   ```
2. Запустите приложение:
   ```sh
   ./gradlew bootRun
   ```

---

## 2.2. Развёртывание с Docker

### &#x20;Требования

- Docker 20+
- Docker Compose (опционально)

### Запуск через Docker

1. Соберите и запустите контейнер:
   ```sh
   docker build -t your-app .
   docker run -p 8080:8080 your-app
   ```

### Запуск через Docker Compose

```sh
docker-compose up -d
```

### Остановка контейнера

```sh
docker stop your-app
```
---
## 3. Конечные точки
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **H2 Console** (База данных): http://localhost:8080/h2-console
  - **url:** `jdbc:h2:file:./data/testdb`
  - **username:** `sa`
  - **пароль пустой**


