# User Order API

Spring Boot REST API，提供用户注册登录、JWT 鉴权、用户资料管理、订单管理与 Swagger 文档。

## 本地启动（开发环境）

### 1) 准备数据库

默认连接参数：

- Host: `localhost`
- Port: `5432`
- Database: `user_order_api`
- Username: `postgres`
- Password: `postgres`

也可以通过环境变量覆盖：

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

### 2) 启动应用

```bash
./mvnw spring-boot:run
```

或显式指定数据库：

```bash
DB_URL=jdbc:postgresql://localhost:5433/user_order_api \
DB_USERNAME=postgres \
DB_PASSWORD=postgres \
./mvnw spring-boot:run
```

### 3) 访问地址

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## 生产启动（prod profile）

生产环境使用 `application-prod.properties`，必须通过环境变量提供数据库和 JWT 密钥。

### 必填环境变量

- `SPRING_PROFILES_ACTIVE=prod`
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`

### 可选环境变量

- `JWT_EXPIRATION_SECONDS`（默认 3600）
- `LOGIN_MAX_FAILED_ATTEMPTS`（默认 5）
- `LOGIN_BLOCK_SECONDS`（默认 300）
- `SERVER_PORT`（默认 8080）

### 启动命令

```bash
SPRING_PROFILES_ACTIVE=prod \
DB_URL=jdbc:postgresql://<host>:5432/user_order_api \
DB_USERNAME=<username> \
DB_PASSWORD=<password> \
JWT_SECRET=<strong_secret> \
./mvnw spring-boot:run
```

## 测试

```bash
DB_URL=jdbc:postgresql://localhost:5433/user_order_api ./mvnw test
```
