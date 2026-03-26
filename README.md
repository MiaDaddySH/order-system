# User Order API

A Spring Boot REST API for user and order management, featuring signup/login, JWT authentication, profile management, order management, database migrations, API documentation, observability, and automated deployment pipelines.

## Highlights

- Authentication: email-based registration/login with JWT auth
- Security: login attempt throttling and protected endpoints
- Data layer: PostgreSQL with Flyway versioned migrations
- Engineering: unit/API tests and CI test workflow
- Operations: Nginx + HTTPS, health checks, production auto-deploy
- Release safety: post-deploy smoke tests with automatic rollback on failure

## Tech Stack

- Java 17
- Spring Boot 4
- Spring Security
- Spring Data JPA
- PostgreSQL
- Flyway
- Maven
- GitHub Actions
- Nginx

## Architecture Overview

`Client -> Nginx(443) -> Spring Boot(8080) -> PostgreSQL`

- Nginx handles TLS termination and reverse proxying
- Spring Boot provides business APIs and authentication
- PostgreSQL stores application data, with Flyway handling schema migration

## Local Run (Development)

### 1) Prepare the database

Default connection settings:

- Host: `localhost`
- Port: `5432`
- Database: `user_order_api`
- Username: `postgres`
- Password: `postgres`

You can override them via environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

### 2) Start the application

```bash
./mvnw spring-boot:run
```

Or start with explicit DB env vars:

```bash
DB_URL=jdbc:postgresql://localhost:5432/user_order_api \
DB_USERNAME=postgres \
DB_PASSWORD=postgres \
./mvnw spring-boot:run
```

### 3) Access endpoints

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Quick API Demo (Local)

```bash
# Health check
curl -s http://localhost:8080/actuator/health

# Register
curl -s -X POST http://localhost:8080/users/register \
  -H "Content-Type: application/json" \
  -d '{"email":"demo@example.com","password":"Password123"}'

# Login
curl -s -X POST http://localhost:8080/users/login \
  -H "Content-Type: application/json" \
  -d '{"email":"demo@example.com","password":"Password123"}'
```

## Tests

```bash
DB_URL=jdbc:postgresql://localhost:5432/user_order_api \
DB_USERNAME=postgres \
DB_PASSWORD=postgres \
./mvnw -B test
```

## Production Deployment (prod)

Production settings are driven by `application-prod.properties` and injected via environment variables:

- Required: `SPRING_PROFILES_ACTIVE=prod`, `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, `CORS_ALLOWED_ORIGINS`
- Optional: `JWT_EXPIRATION_SECONDS`, `LOGIN_MAX_FAILED_ATTEMPTS`, `LOGIN_BLOCK_SECONDS`, `SERVER_PORT`

Environment template:

```bash
cp deploy/prod.env.example .env.prod
```

Nginx config template:

```bash
cp deploy/nginx/user-order-api.conf.example /etc/nginx/conf.d/user-order-api.conf
nginx -t && nginx -s reload
```

Go-live checklist:

- [PRODUCTION_CHECKLIST.md](deploy/PRODUCTION_CHECKLIST.md)

## CI/CD Pipeline

- `main` branch triggers the `CD Deploy Prod` workflow
- Flow: test -> package -> upload via SSH -> restart service -> health check -> smoke test
- Smoke test: register -> login -> create order -> query orders
- If health check or smoke test fails, the previous JAR is restored automatically

Workflow files:

- [ci-test.yml](.github/workflows/ci-test.yml)
- [cd-deploy-prod.yml](.github/workflows/cd-deploy-prod.yml)

## Monitoring & Alerts

- Health check: `/actuator/health`
- Basic info: `/actuator/info`
- Prometheus metrics: `/actuator/prometheus`
- Alert rules template: [prometheus-alerts.example.yml](deploy/monitoring/prometheus-alerts.example.yml)

## Project Structure

```text
src/main/java/com/example/userorderapi
├── controller
├── service
├── repository
├── security
├── dto
└── mapper
```
