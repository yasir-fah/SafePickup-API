# SafePickup — CLAUDE.md

## Project Overview

School student pickup safety system. Parents request student release, system validates proximity via GPS, sends OTP via Twilio SMS, and logs the exit. Admins manage users; NFC cards are linked to students for physical verification.

## Tech Stack

- **Java 17**, **Spring Boot 4.0.3**, **Maven**
- **MySQL** via Spring Data JPA / Hibernate (`ddl-auto=update`)
- **Lombok** (`@Data`, `@RequiredArgsConstructor`) — used on all entities and services
- **Twilio SDK v10.1.0** — OTP via Verify API
- **HERE Traffic API** — congestion detection by GPS radius
- **SpringDoc OpenAPI 2.8.5** — Swagger UI at `/swagger-ui.html`

## Key Directories

| Path | Purpose |
|------|---------|
| `src/main/java/.../Controller/` | REST endpoints (`api/v1/` prefix) |
| `src/main/java/.../Service/` | Business logic; services may call other services |
| `src/main/java/.../Repository/` | JPA repositories (method-name queries only) |
| `src/main/java/.../Model/` | JPA entities |
| `src/main/java/.../DTOin/` | Validated request bodies |
| `src/main/java/.../DTOout/` | API response shapes |
| `src/main/java/.../Advise/` | `@ControllerAdvice` global exception handler |
| `src/main/java/.../Api/` | `ApiResponse` wrapper + `ApiException` |
| `src/main/java/.../config/` | Spring beans (`RestClient.Builder`) |
| `src/main/resources/application.properties` | DB, JPA, HERE API, Twilio config |

## Build & Test Commands

```bash
./mvnw spring-boot:run      # Run locally
./mvnw clean package        # Build JAR
./mvnw test                 # Run tests
```

On Windows use `mvnw.cmd` instead of `./mvnw`.

## Environment Variables Required

```
HERE_API_KEY
TWILIO_ACCOUNT_SID
TWILIO_AUTH-TOKEN
TWILIO_SERVICE_SID
```

## Entity Relationships

```
User (1-to-1) → Admin
User (1-to-1) → Parent (1-to-many) → Student (1-to-many) → NfcCard
                                      Student (1-to-many) → ExitLog
```

## Additional Documentation

- [Architectural Patterns](.claude/docs/architectural_patterns.md) — layered architecture, DTO mapping, exception handling, service composition, external API integration