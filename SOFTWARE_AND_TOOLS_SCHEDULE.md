# SafePickup - Software Requirements & Tools Schedule

---

## **SCHEDULE 1: SOFTWARE REQUIREMENTS** 
*(Required to RUN the application in production/staging)*

| # | Software | Version | Type | Purpose | Installation |
|---|----------|---------|------|---------|--------------|
| 1 | **Java JDK** | 17 (LTS) | Runtime | Executes the compiled JAR bytecode | `java -version` to verify |
| 2 | **MySQL Server** | 5.7+ or 8.0+ | Database | Stores user, student, NFC card, exit log data | `mysql --version` to verify |
| 3 | **Twilio** | Cloud Service | External API | Sends OTP via SMS for student release verification | Requires account + credentials (TWILIO_ACCOUNT_SID, TWILIO_AUTH-TOKEN, TWILIO_SERVICE_SID) |
| 4 | **HERE Traffic API** | Cloud Service | External API | Provides GPS proximity & traffic congestion detection | Requires API key (HERE_API_KEY) |

**How to Deploy:**
```bash
java -jar SafePickup.jar
# Application runs with MySQL + external APIs
```

---

## **SCHEDULE 2: BUILD & DEVELOPMENT TOOLS**
*(Required to DEVELOP, BUILD, and TEST the application)*

### **A. Build Tool**

| # | Tool | Version | Purpose | Command |
|---|------|---------|---------|---------|
| 1 | **Maven** | 3.6+ | Dependency management, compilation, testing, packaging | `mvn clean package` |

---

### **B. Core Frameworks & Libraries** *(Managed by Maven)*

| # | Framework/Library | Version | Category | Purpose |
|---|-------------------|---------|----------|---------|
| 1 | **Spring Boot** | 4.0.3 | Framework | REST API development, auto-configuration, embedded server |
| 2 | **Spring Security** | (via Spring Boot) | Security | Authentication, authorization, CORS policies |
| 3 | **Spring Data JPA** | (via Spring Boot) | ORM | Database object-relational mapping |
| 4 | **Hibernate** | (via Spring Boot) | ORM | JPA implementation, entity management |
| 5 | **Spring Web/MVC** | (via Spring Boot) | Web | REST endpoint handling, request/response mapping |
| 6 | **Spring Validation** | (via Spring Boot) | Validation | Bean validation for DTOs, input constraints |

---

### **C. Code Generation & Productivity**

| # | Library | Version | Purpose |
|---|---------|---------|---------|
| 1 | **Lombok** | Latest (via Spring Boot) | Generates getters, setters, constructors, equals/hashCode via annotations |

---

### **D. Data Handling & Serialization**

| # | Library | Version | Purpose |
|---|---------|---------|---------|
| 1 | **Jackson** | (via Spring Boot) | JSON serialization/deserialization for DTOs |
| 2 | **MySQL Connector/J** | Latest | JDBC driver for MySQL database connectivity |

---

### **E. Authentication & Tokens**

| # | Library | Version | Purpose |
|---|---------|---------|---------|
| 1 | **JJWT (Java JWT)** | 0.11.5 | JSON Web Token creation, validation, and parsing |
| 2 | **JJWT Impl** | 0.11.5 | JWT implementation (runtime) |
| 3 | **JJWT Jackson** | 0.11.5 | JWT Jackson integration (runtime) |

---

### **F. API Documentation**

| # | Tool | Version | Purpose |
|---|------|---------|---------|
| 1 | **SpringDoc OpenAPI** | 2.8.5 | Auto-generates Swagger UI at `/swagger-ui.html` |

---

### **G. External SDK**

| # | Library | Version | Purpose |
|---|---------|---------|---------|
| 1 | **Twilio SDK** | 10.1.0 | Programmatic SMS delivery for OTP |

---

### **H. Testing Libraries** *(Scope: test)*

| # | Library | Version | Purpose |
|---|---------|---------|---------|
| 1 | **Spring Boot Test Starter** | (via Spring Boot) | JUnit integration, test context, mocking |
| 2 | **Spring Data JPA Test** | (via Spring Boot) | JPA-specific test utilities, test database |
| 3 | **Spring Validation Test** | (via Spring Boot) | Validation testing utilities |
| 4 | **Spring Web/MVC Test** | (via Spring Boot) | REST endpoint testing, MockMvc |

---

### **I. Build Plugins**

| # | Plugin | Purpose | Configuration |
|---|--------|---------|---------------|
| 1 | **Maven Compiler Plugin** | Compiles Java 17 code with Lombok annotation processing | Lombok processor configured in `<annotationProcessorPaths>` |
| 2 | **Spring Boot Maven Plugin** | Packages JAR; enables `mvn spring-boot:run` | Excludes Lombok from final JAR |

---

## **SCHEDULE 3: DEVELOPMENT TOOLS & IDE**
*(Optional for developers, NOT needed for deployment)*

| # | Tool | Type | Purpose | Notes |
|---|------|------|---------|-------|
| 1 | **IntelliJ IDEA / VS Code / Eclipse** | IDE | Code editing, debugging, refactoring | IntelliJ recommended (has best Spring Boot support) |
| 2 | **Git** | Version Control | Track code changes, branching | Already in use (current branch: `yasiraltamimi178/saf-27-implement-cors-policy`) |
| 3 | **Postman / Insomnia / Thunder Client** | API Testing | Test REST endpoints during development | Optional (Swagger UI at `/swagger-ui.html` also works) |
| 4 | **MySQL Workbench / DBeaver** | Database GUI | Browse/query database during development | Optional (can use CLI instead) |

---

## **SCHEDULE 4: ENVIRONMENT VARIABLES REQUIRED**
*(Set before running the application)*

| Variable | Source | Purpose | Required For |
|----------|--------|---------|--------------|
| `HERE_API_KEY` | HERE Traffic API account | GPS proximity & congestion detection | Production/Staging |
| `TWILIO_ACCOUNT_SID` | Twilio account | SMS OTP delivery | Production/Staging |
| `TWILIO_AUTH-TOKEN` | Twilio account | Twilio authentication | Production/Staging |
| `TWILIO_SERVICE_SID` | Twilio Verify service | OTP service identification | Production/Staging |
| `JWT_SECRET` | Generated/configured | JWT token signing | Production/Staging |

---

## **QUICK REFERENCE: What You Need**

### **To RUN the app:**
```
✓ Java 17
✓ MySQL running on localhost:3306
✓ Twilio credentials (env vars)
✓ HERE API key (env var)
✓ Compiled JAR file
```

### **To BUILD/DEVELOP the app:**
```
✓ Java 17 JDK (with javac)
✓ Maven
✓ Git (for version control)
✓ IDE (optional but recommended)
✓ All dependencies listed in pom.xml (auto-downloaded by Maven)
```

### **Build Command:**
```bash
mvn clean package
# Produces: target/SafePickup-0.0.1-SNAPSHOT.jar
```

### **Run Command:**
```bash
java -jar target/SafePickup-0.0.1-SNAPSHOT.jar
# Application starts on http://localhost:8080
```

---