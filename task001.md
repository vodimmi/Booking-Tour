# 🚀 Phase 1 — Core Auth Service Implementation Plan

## 🎯 Project Scope (Auth Service Features)

| ID     | Feature              | Description                                                                   |
| :----- | :------------------- | :---------------------------------------------------------------------------- |
| **A1** | User Registration    | API for new user account registration.                                        |
| **A2** | User Login           | API for user authentication and login.                                        |
| **A3** | Refresh Token        | API to obtain a new token pair (Access & Refresh) using an old Refresh Token. |
| **A4** | Get Profile (Me)     | Secured API to retrieve the current user's profile information.               |
| **J1** | JWT Generation       | Generate JWT (Access Token) using the **RS256** algorithm.                    |
| **J2** | Refresh Token Mgmt   | Store, validate, and revoke Refresh Tokens in the database.                   |
| **J3** | JWKS Endpoint        | Provide the Public Key (JWKS) for other services to validate JWTs.            |
| **I1** | OpenAPI Docs         | API documentation (Swagger UI) using `springdoc`.                             |
| **I2** | Flyway Migration     | Initial database schema management.                                           |
| **I3** | Actuator Healthcheck | Service health check endpoint.                                                |

---

## 📂 Module Structure (module: `auth`)

### Directories

- `src/main/java/com/example/auth/{controller,service,repository,entity,security,config,exception}`
- `src/main/resources/{application.yml, db/migration/V1__init_auth.sql}`

### Core Components to Implement

- **Entities**: User, Role, RefreshToken
- **Repositories**: UserRepository, RoleRepository, RefreshTokenRepository
- **Security**: KeyProvider (RSA 2048 in-memory), JwtService (RS256)
- **Controllers**: AuthController, JwksController
- **Configuration**: SecurityConfig, PasswordConfig
- **Utilities**: Basic exception handler

---

## 💻 API Endpoints

| Method | Path                 | Auth             | Description                           | Body / Response Format                                      |
| :----- | :------------------- | :--------------- | :------------------------------------ | :---------------------------------------------------------- |
| `POST` | `/api/auth/register` | `N/A`            | Register (email, password, fullName). | `returns {accessToken, refreshToken}`                       |
| `POST` | `/api/auth/login`    | `N/A`            | Login (email, password).              | `returns {accessToken, refreshToken}`                       |
| `POST` | `/api/auth/refresh`  | `N/A`            | Obtain new tokens.                    | `body {refreshToken} → returns {accessToken, refreshToken}` |
| `GET`  | `/api/auth/me`       | **Bearer Token** | Retrieve current user profile.        | `returns {id, email, fullName, roles}`                      |
| `GET`  | `/jwks.json`         | `N/A`            | Public Key Endpoint (JWKS).           | JSON (RFC 7517)                                             |
| `GET`  | `/actuator/health`   | `N/A`            | Health Check.                         | `status = UP`                                               |

---

## 💾 Data Model (Database Schema & Flyway)

### Tables

- `users(id, email, password_hash, full_name, is_active, created_at, updated_at)`
- `roles(id, name)`
- `user_roles(user_id, role_id)`
- `refresh_tokens(id, user_id, token, expires_at, revoked, created_at)`

### Seed Data

- `roles`: **USER**, **ADMIN**

### Security & Token Hints

- **Access Token TTL**: 30 minutes.
- **Refresh Token TTL**: 7 days.
- **Refresh Token**: Store UUIDv4 in DB, manage `revoked` status.
- **JWT Claims**: `sub`=userId, `email`, `roles`, `iss`=booking-tour-auth.
- **Password Hashing**: **BCrypt**.

---

---

# ✅ Implementation & Verification Checklist (Copilot Checklist)

| ID     | Feature / Component   | Status | Verification (Acceptance Criteria)                                                                             |
| :----- | :-------------------- | :----- | :------------------------------------------------------------------------------------------------------------- |
| **S1** | **Setup & Migration** | [ ]    | **`mvn -pl auth spring-boot:run`** starts OK, Flyway successfully executed (`V1__init_auth.sql`).              |
| **S2** | **Data Layer**        | [ ]    | Entities (`User`, `Role`, `RefreshToken`) & Repositories are functional.                                       |
| **S3** | **Password Hashing**  | [ ]    | BCryptPasswordEncoder bean is configured.                                                                      |
| **S4** | **Key Generation**    | [ ]    | KeyProvider generates and stores **RSA 2048-bit** key pair.                                                    |
| **S5** | **Security Config**   | [ ]    | Spring Security configured as **stateless** and **permits** public routes.                                     |
| **A1** | **Registration**      | [ ]    | `POST /api/auth/register` creates user, returns valid `{accessToken, refreshToken}`. Default role is **USER**. |
| **A2** | **Login**             | [ ]    | `POST /api/auth/login` (with created user) returns valid token pair.                                           |
| **A3** | **Refresh Token**     | [ ]    | `POST /api/auth/refresh` returns a new token pair. The old refresh token is **revoked** in DB.                 |
| **A4** | **Get Profile (Me)**  | [ ]    | `GET /api/auth/me` (Bearer Token) returns correct user data (`id, email, fullName, roles`).                    |
| **J3** | **JWKS Endpoint**     | [ ]    | `GET /jwks.json` returns the Public Key (kid matches JWT header).                                              |
| **I1** | **OpenAPI Docs**      | [ ]    | Swagger UI available at `/swagger-ui/index.html` and lists all 4 main APIs.                                    |
| **I3** | **Actuator**          | [ ]    | `GET /actuator/health` returns `status = UP`.                                                                  |
| **E1** | **Exception Handler** | [ ]    | Basic handling for errors like Invalid Credentials/User Not Found (returning 400/401/404).                     |
