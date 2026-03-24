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
- `CORS_ALLOWED_ORIGINS`

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
CORS_ALLOWED_ORIGINS=https://app.example.com,https://admin.example.com \
./mvnw spring-boot:run
```

### 生产环境变量模板

可以复制并按实际环境填写：

```bash
cp deploy/prod.env.example .env.prod
```

### HTTPS 反向代理（Nginx 示例）

生产建议由 Nginx 终止 TLS，再反向代理到应用服务。

```bash
cp deploy/nginx/user-order-api.conf.example /etc/nginx/conf.d/user-order-api.conf
```

需要按实际环境调整：

- `server_name`（域名）
- `ssl_certificate` 与 `ssl_certificate_key`（证书路径）
- `proxy_pass`（应用监听地址与端口）

修改后执行：

```bash
nginx -t && nginx -s reload
```

### 生产上线清单

上线前请逐项检查：

- [PRODUCTION_CHECKLIST.md](file:///Volumes/Development/intellij_projects/demo/deploy/PRODUCTION_CHECKLIST.md)

## 测试

```bash
DB_URL=jdbc:postgresql://localhost:5433/user_order_api ./mvnw test
```
