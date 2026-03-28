# Architectural Patterns

Patterns observed across multiple files in SafePickup.

## 1. Layered Architecture (Controller → Service → Repository)

All features follow the same flow. Controllers delegate entirely to services; services own all business logic and call repositories directly. No logic lives in controllers beyond request parsing and response wrapping.

- `StudentController` → `StudentService` → `StudentRepository`
- `ParentController` → `ParentService` → `ParentRepository`, `CongestionService`, `TwilioVerifyService`

Dependency injection uses Lombok `@RequiredArgsConstructor` + `final` fields on every service and controller. No `@Autowired`.

## 2. DTO Split: DTOin / DTOout

All API input goes through a `DTOin` class with Jakarta validation annotations (`@NotEmpty`, `@NotNull`, `@Size`, `@Pattern`). Controllers declare `@Valid @RequestBody`.

All API output goes through a `DTOout` class constructed from an entity:
```
new StudentResponseDTO(student)  // entity passed into DTO constructor
```
Entities are never returned directly from controllers.

## 3. `ApiResponse` Wrapper

Successful responses that don't return a domain object use `new ApiResponse("message")`. See `Api/ApiResponse.java`. Returns: `ResponseEntity.status(200).body(new ApiResponse(...))`.

## 4. Centralized Exception Handling

`Advise/ControllerAdvise.java` (`@ControllerAdvice`) handles all exceptions:
- `ApiException` → 400 (thrown explicitly in services for business rule violations)
- `MethodArgumentNotValidException` → 400 with `Map<field, message>`
- `DataIntegrityViolationException` / `SQLIntegrityConstraintViolationException` → 400
- `ConstraintViolationException` → 400
- `HttpRequestMethodNotSupportedException` → 405
- `NoResourceFoundException` → 404
- `Exception` → 500 (logged)

Services throw `ApiException` (extends `RuntimeException`) for all business errors; they never return error codes.

## 5. Service Composition

Services are injected into other services for cross-domain logic. Example: `ParentService` holds references to `CongestionService` and `TwilioVerifyService`. This is intentional — no facade layer is used.

## 6. External API Integration Pattern

`CongestionService` (HERE API) and `TwilioVerifyService` follow the same pattern:
- Config values injected via `@Value("${property.key}")`
- Third-party client initialized in `@PostConstruct`
- `CongestionService` uses Spring's `RestClient` (bean provided in `config/AppConfig.java`)
- `TwilioVerifyService` uses `Twilio.init(sid, token)` in `@PostConstruct`

## 7. JPA Repository: Derived Query Methods Only

All repositories extend `JpaRepository<Entity, ID>` and use Spring Data method-name conventions exclusively. No `@Query` annotations are used.

Example: `NfcCard findNfcCardByStudent_Id(Integer studentId)` — underscore traverses the relationship.

## 8. Entity JSON Serialization Control

`@JsonIgnore` is placed on the "many" side of bidirectional relationships (e.g., `Student.parent`, `ExitLog.student`) to prevent circular serialization. No custom serializers are used.

## 9. One-to-One Inheritance via `@MapsId`

`Admin` and `Parent` share the same primary key as `User` using `@MapsId` on the `@OneToOne` relation. This means `admin.getId() == admin.getUser().getId()`.
