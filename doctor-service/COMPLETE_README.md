# Doctor Service - Complete Spring Boot Application

A modern, production-grade Spring Boot microservice for managing doctors in a Patient Management System. This service follows the latest Spring Boot best practices with comprehensive CRUD operations, validation, exception handling, and API documentation.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Configuration](#configuration)
- [Database](#database)
- [Exception Handling](#exception-handling)
- [Validation](#validation)
- [Testing](#testing)
- [Swagger Documentation](#swagger-documentation)

## ✨ Features

### Core CRUD Operations
- ✅ Create new doctor
- ✅ Read doctor by ID or email
- ✅ Update doctor (full and partial)
- ✅ Delete doctor
- ✅ List all doctors with pagination
- ✅ Toggle doctor status (active/inactive)

### Advanced Features
- ✅ Search doctors by name, email, or specialization
- ✅ Filter doctors by specialization
- ✅ Get active doctors only
- ✅ Check doctor existence by email or phone
- ✅ Count active doctors
- ✅ Comprehensive validation
- ✅ Global exception handling
- ✅ Request/Response DTOs
- ✅ Pagination and sorting support

### Non-Functional Requirements
- ✅ Swagger/OpenAPI documentation
- ✅ Comprehensive logging
- ✅ CORS support
- ✅ Database connection pooling
- ✅ Input validation with detailed error messages
- ✅ Unit and integration tests
- ✅ RESTful API design
- ✅ Transaction management

## 🛠 Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 17 | Programming Language |
| Spring Boot | 4.0.5 | Framework |
| Spring Data JPA | - | ORM |
| Spring Validation | - | Input Validation |
| MySQL | 8.0+ | Database |
| Lombok | Latest | Boilerplate Reduction |
| SpringDoc OpenAPI | 2.3.0 | API Documentation |
| JUnit 5 | Latest | Testing |
| Mockito | Latest | Mocking |
| Maven/Gradle | - | Build Tool |

## 📁 Project Structure

```
doctor-service/
├── src/
│   ├── main/
│   │   ├── java/com/pm/doctorservice/
│   │   │   ├── config/              # Spring configuration classes
│   │   │   │   ├── CorsConfig.java           # CORS configuration
│   │   │   │   ├── JpaConfig.java            # JPA configuration
│   │   │   │   └── SwaggerConfig.java        # Swagger configuration
│   │   │   ├── controller/          # REST controllers
│   │   │   │   └── DoctorController.java     # Doctor endpoints
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── ApiResponse.java          # Generic response wrapper
│   │   │   │   ├── DoctorDTO.java            # Doctor DTO
│   │   │   │   └── DoctorRequestDTO.java     # Doctor request DTO
│   │   │   ├── exception/           # Custom exceptions
│   │   │   │   ├── DuplicateResourceException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── InvalidInputException.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── model/               # JPA entities
│   │   │   │   └── Doctor.java               # Doctor entity
│   │   │   ├── repository/          # Data access layer
│   │   │   │   └── DoctorRepository.java     # Doctor repository
│   │   │   ├── service/             # Business logic layer
│   │   │   │   ├── DoctorService.java        # Service interface
│   │   │   │   └── impl/
│   │   │   │       └── DoctorServiceImpl.java # Service implementation
│   │   │   ├── util/                # Utility classes
│   │   │   │   ├── DoctorMapper.java         # Entity-DTO mapping
│   │   │   │   └── IdGenerator.java          # ID generation
│   │   │   └── DoctorServiceApplication.java # Main application class
│   │   └── resources/
│   │       ├── application.properties        # Application configuration
│   │       ├── schema.sql                    # Database schema
│   │       ├── data.sql                      # Sample data
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/com/pm/doctorservice/
│           ├── controller/
│           │   └── DoctorControllerTest.java
│           └── service/
│               └── DoctorServiceTest.java
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
└── README.md
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- MySQL 8.0 or higher
- Gradle or Maven
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Installation

1. **Clone the repository**
```bash
cd patient-management/doctor-service
```

2. **Configure Database**
   - Create MySQL database:
   ```sql
   CREATE DATABASE doctor_db;
   ```
   - Update credentials in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/doctor_db
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

3. **Build the Project**
```bash
# Using Gradle
./gradlew build

# Using Maven
mvn clean install
```

4. **Run the Application**
```bash
# Using Gradle
./gradlew bootRun

# Using Maven
mvn spring-boot:run
```

5. **Verify Application**
   - Application starts on: `http://localhost:8081`
   - Swagger UI: `http://localhost:8081/doctor-service/swagger-ui.html`
   - API Docs: `http://localhost:8081/doctor-service/v3/api-docs`

## 🔌 API Endpoints

### Base URL
```
http://localhost:8081/doctor-service/api/v1
```

### Create Doctor
```http
POST /doctors
Content-Type: application/json

{
  "doctorId": "DOC001",
  "doctorName": "Dr. John Doe",
  "specialization": "Cardiology",
  "doctorEmail": "john.doe@hospital.com",
  "doctorPhone": "+1-234-567-8900",
  "joiningDate": "2020-01-15"
}

Response: 201 Created
{
  "statusCode": 201,
  "message": "Doctor created successfully",
  "data": { ... },
  "timestamp": "2024-01-15T10:30:00"
}
```

### Get Doctor by ID
```http
GET /doctors/{doctorId}

Response: 200 OK
{
  "statusCode": 200,
  "message": "Doctor retrieved successfully",
  "data": { ... },
  "timestamp": "2024-01-15T10:30:00"
}
```

### Get All Doctors (Paginated)
```http
GET /doctors?page=0&size=10&sortBy=doctorId&direction=ASC

Response: 200 OK
{
  "statusCode": 200,
  "message": "Doctors retrieved successfully",
  "data": {
    "content": [ ... ],
    "pageable": { ... },
    "totalElements": 50,
    "totalPages": 5
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### Update Doctor (Full Update)
```http
PUT /doctors/{doctorId}
Content-Type: application/json

{
  "doctorName": "Dr. John Smith",
  "specialization": "Neurology",
  "doctorEmail": "john.smith@hospital.com",
  "doctorPhone": "+1-234-567-8910",
  "joiningDate": "2020-01-15"
}

Response: 200 OK
```

### Partial Update Doctor
```http
PATCH /doctors/{doctorId}
Content-Type: application/json

{
  "specialization": "Neurology"
}

Response: 200 OK
```

### Delete Doctor
```http
DELETE /doctors/{doctorId}

Response: 204 No Content
```

### Get Active Doctors
```http
GET /doctors/active?page=0&size=10

Response: 200 OK
```

### Get Doctors by Specialization
```http
GET /doctors/specialization/{specialization}

Response: 200 OK
```

### Get Active Doctors by Specialization
```http
GET /doctors/specialization/{specialization}/active?page=0&size=10

Response: 200 OK
```

### Toggle Doctor Status
```http
POST /doctors/{doctorId}/toggle-status

Response: 200 OK
```

### Search Doctors
```http
GET /doctors/search?searchTerm=john&page=0&size=10

Response: 200 OK
```

### Get Doctor by Email
```http
GET /doctors/email/{email}

Response: 200 OK
```

### Check Doctor Exists by Email
```http
GET /doctors/exists/email/{email}

Response: 200 OK
{
  "statusCode": 200,
  "data": true
}
```

### Check Doctor Exists by Phone
```http
GET /doctors/exists/phone/{phone}

Response: 200 OK
{
  "statusCode": 200,
  "data": true
}
```

### Get Active Doctors Count
```http
GET /doctors/count/active

Response: 200 OK
{
  "statusCode": 200,
  "data": 45
}
```

## ⚙️ Configuration

### application.properties

```properties
# Server Configuration
spring.application.name=doctor-service
server.port=8081
server.servlet.context-path=/doctor-service

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/doctor_db
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# Logging
logging.level.com.pm.doctorservice=DEBUG
logging.level.org.hibernate.SQL=DEBUG

# Swagger/OpenAPI
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs

# Connection Pool
spring.datasource.hikari.maximum-pool-size=10
```

## 💾 Database

### Schema
The database automatically creates the `doctors` table with the following structure:

```sql
CREATE TABLE doctors (
    doctor_id VARCHAR(50) PRIMARY KEY,
    doctor_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(50) NOT NULL,
    doctor_email VARCHAR(100) NOT NULL UNIQUE,
    doctor_phone VARCHAR(20) NOT NULL,
    joining_date DATE NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Indexes
- `idx_doctor_email` - Unique index on doctor_email for fast lookups
- `idx_doctor_phone` - Index on doctor_phone
- `idx_specialization` - Index on specialization

## ❌ Exception Handling

### Custom Exceptions

| Exception | HTTP Status | Scenario |
|-----------|-------------|----------|
| `ResourceNotFoundException` | 404 | Doctor not found |
| `DuplicateResourceException` | 409 | Email or phone already exists |
| `InvalidInputException` | 400 | Invalid input parameters |
| `MethodArgumentNotValidException` | 400 | Validation failed |

### Global Exception Handler
All exceptions are centrally handled by `GlobalExceptionHandler` with consistent response format:

```json
{
  "statusCode": 400,
  "message": "Validation failed",
  "error": "Doctor name cannot be blank",
  "timestamp": "2024-01-15T10:30:00"
}
```

## ✔️ Validation

### Doctor Entity Validations

| Field | Constraints |
|-------|------------|
| `doctorName` | Required, 3-100 characters |
| `specialization` | Required, 3-50 characters |
| `doctorEmail` | Required, valid email format, unique |
| `doctorPhone` | Required, 10+ digits, unique |
| `joiningDate` | Required, cannot be in future |

### Example Validation Error Response
```json
{
  "statusCode": 400,
  "message": "Validation failed",
  "error": "{doctorEmail=Email should be valid, doctorPhone=Phone number must be at least 10 digits}",
  "timestamp": "2024-01-15T10:30:00"
}
```

## 🧪 Testing

### Running Tests
```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests DoctorServiceTest

# Run with coverage
./gradlew test jacocoTestReport
```

### Test Coverage
- **DoctorServiceTest** - 15+ test cases for service layer
- **DoctorControllerTest** - 10+ test cases for REST endpoints
- Coverage includes: CRUD operations, validation, exception handling

## 📚 Swagger Documentation

### Access Swagger UI
```
http://localhost:8081/doctor-service/swagger-ui.html
```

### Features
- Complete API documentation with descriptions
- Try out feature to test endpoints
- Request/response examples
- Parameter descriptions
- Error response documentation

## 🏗️ Architecture

### Layered Architecture

```
┌─────────────────────────────────────┐
│         REST Controller              │
│    (Handles HTTP Requests)           │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Service Layer (Business Logic)  │
│    (Implements use cases)            │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│    Repository Layer (Data Access)   │
│    (JPA/Hibernate)                   │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Database (MySQL)             │
│    (Persistent Storage)              │
└─────────────────────────────────────┘
```

## 🔒 Security Considerations

- ✅ Input validation on all endpoints
- ✅ SQL injection prevention (JPA parameterized queries)
- ✅ CORS configured for cross-origin requests
- ✅ Request/Response validation with proper error handling
- ✅ Unique constraints on email and phone
- ✅ Transaction management

## 📝 Best Practices Implemented

- ✅ RESTful API design
- ✅ Proper HTTP status codes
- ✅ Consistent response format
- ✅ Comprehensive logging
- ✅ DTOs for request/response
- ✅ Separation of concerns
- ✅ Transaction management
- ✅ Exception handling
- ✅ Pagination support
- ✅ Input validation
- ✅ API documentation
- ✅ Unit and integration tests

## 📞 Support

For issues or questions, please contact the development team or create an issue in the repository.

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Development Team

- Patient Management System Team
- Email: support@patientmanagement.com

---

**Last Updated:** January 2024
**Version:** 1.0.0
