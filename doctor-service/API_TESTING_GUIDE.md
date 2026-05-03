# Doctor Service - API Testing Guide

## 📋 Overview

This guide provides detailed instructions for testing the Doctor Service API using various tools and methods.

---

## 🧪 Testing Tools

### 1. Postman
- Industry standard for API testing
- Collections provided below
- Environment variables for easy testing
- Pre/post request scripts available

### 2. cURL
- Command-line HTTP client
- Great for CI/CD pipelines
- Quick testing without GUI

### 3. Insomnia
- User-friendly alternative to Postman
- Similar collection format
- Good request chaining support

### 4. Thunder Client (VS Code)
- Built into VS Code
- Lightweight and fast
- Great for developers

### 5. Unit Tests
- JUnit 5 tests included
- Mockito for mocking
- Run with: `./gradlew test`

---

## 🔑 Environment Variables

Create these variables in your testing tool:

```
base_url = http://localhost:8081/doctor-service
api_version = /api/v1
content_type = application/json
```

---

## 📝 Test Scenarios

### Scenario 1: Create Doctor
**Endpoint:** `POST /api/v1/doctors`

**Request:**
```json
{
  "doctorId": "DOC001",
  "doctorName": "Dr. John Doe",
  "specialization": "Cardiology",
  "doctorEmail": "john.doe@hospital.com",
  "doctorPhone": "+1-234-567-8900",
  "joiningDate": "2020-01-15"
}
```

**Expected Response:**
- Status: `201 Created`
- Response Body:
```json
{
  "statusCode": 201,
  "message": "Doctor created successfully",
  "data": {
    "doctorId": "DOC001",
    "doctorName": "Dr. John Doe",
    "specialization": "Cardiology",
    "doctorEmail": "john.doe@hospital.com",
    "doctorPhone": "+1-234-567-8900",
    "joiningDate": "2020-01-15",
    "isActive": true
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

**Validation Tests:**
- [ ] Status code is 201
- [ ] Response contains doctorId
- [ ] isActive defaults to true
- [ ] timestamp is present

---

### Scenario 2: Validate Duplicate Email
**Endpoint:** `POST /api/v1/doctors`

**Request:** (Same doctorEmail as previous)
```json
{
  "doctorId": "DOC002",
  "doctorName": "Dr. Jane Smith",
  "specialization": "Neurology",
  "doctorEmail": "john.doe@hospital.com",
  "doctorPhone": "+1-234-567-8901",
  "joiningDate": "2020-01-15"
}
```

**Expected Response:**
- Status: `409 Conflict`
- Response Body:
```json
{
  "statusCode": 409,
  "message": "Duplicate resource",
  "error": "Doctor already exists with email: john.doe@hospital.com",
  "timestamp": "2024-01-15T10:31:00"
}
```

**Validation Tests:**
- [ ] Status code is 409
- [ ] Error message mentions email exists
- [ ] Doctor not created

---

### Scenario 3: Validate Input Validation
**Endpoint:** `POST /api/v1/doctors`

**Request:** (Invalid email format)
```json
{
  "doctorId": "DOC003",
  "doctorName": "Dr. Test",
  "specialization": "Orthopedics",
  "doctorEmail": "invalid-email",
  "doctorPhone": "+1-234-567-8902",
  "joiningDate": "2020-01-15"
}
```

**Expected Response:**
- Status: `400 Bad Request`
- Error message mentions invalid email

**Validation Tests:**
- [ ] Status code is 400
- [ ] Error mentions email validation
- [ ] Doctor not created

---

### Scenario 4: Get Doctor by ID
**Endpoint:** `GET /api/v1/doctors/{doctorId}`

**Example:** `GET /api/v1/doctors/DOC001`

**Expected Response:**
- Status: `200 OK`
- Response includes all doctor fields

**Validation Tests:**
- [ ] Status code is 200
- [ ] Correct doctor returned
- [ ] All fields present

---

### Scenario 5: Get Non-existent Doctor
**Endpoint:** `GET /api/v1/doctors/{doctorId}`

**Example:** `GET /api/v1/doctors/INVALID_ID`

**Expected Response:**
- Status: `404 Not Found`
- Error message mentions doctor not found

**Validation Tests:**
- [ ] Status code is 404
- [ ] Proper error message

---

### Scenario 6: Get All Doctors (Paginated)
**Endpoint:** `GET /api/v1/doctors?page=0&size=10`

**Expected Response:**
- Status: `200 OK`
- Contains pagination information
- Array of doctors

**Test with Different Parameters:**
- `?page=0&size=5` (First 5)
- `?page=1&size=10` (Second page)
- `?sortBy=doctorName&direction=DESC` (Sort by name descending)

**Validation Tests:**
- [ ] Status code is 200
- [ ] content array present
- [ ] totalElements correct
- [ ] totalPages calculated correctly
- [ ] pageable information included

---

### Scenario 7: Update Doctor (Full)
**Endpoint:** `PUT /api/v1/doctors/{doctorId}`

**Example:** `PUT /api/v1/doctors/DOC001`

**Request:**
```json
{
  "doctorName": "Dr. John Smith",
  "specialization": "Neurology",
  "doctorEmail": "john.smith@hospital.com",
  "doctorPhone": "+1-234-567-8910",
  "joiningDate": "2020-01-15"
}
```

**Expected Response:**
- Status: `200 OK`
- Response includes updated data

**Validation Tests:**
- [ ] Status code is 200
- [ ] All fields updated
- [ ] Old values replaced

---

### Scenario 8: Partial Update Doctor
**Endpoint:** `PATCH /api/v1/doctors/{doctorId}`

**Example:** `PATCH /api/v1/doctors/DOC001`

**Request:** (Update only specialization)
```json
{
  "specialization": "Cardiology"
}
```

**Expected Response:**
- Status: `200 OK`
- Only specified field updated

**Validation Tests:**
- [ ] Status code is 200
- [ ] Specified field changed
- [ ] Other fields unchanged

---

### Scenario 9: Delete Doctor
**Endpoint:** `DELETE /api/v1/doctors/{doctorId}`

**Example:** `DELETE /api/v1/doctors/DOC001`

**Expected Response:**
- Status: `204 No Content`
- No response body

**Validation Tests:**
- [ ] Status code is 204
- [ ] Subsequent GET returns 404
- [ ] Doctor removed from database

---

### Scenario 10: Get Doctors by Specialization
**Endpoint:** `GET /api/v1/doctors/specialization/{specialization}`

**Example:** `GET /api/v1/doctors/specialization/Cardiology`

**Expected Response:**
- Status: `200 OK`
- Array of doctors with that specialization

**Validation Tests:**
- [ ] Status code is 200
- [ ] All returned doctors have correct specialization
- [ ] Correct count

---

### Scenario 11: Get Active Doctors
**Endpoint:** `GET /api/v1/doctors/active?page=0&size=10`

**Expected Response:**
- Status: `200 OK`
- All doctors have isActive = true

**Validation Tests:**
- [ ] Status code is 200
- [ ] All doctors are active
- [ ] Pagination working

---

### Scenario 12: Toggle Doctor Status
**Endpoint:** `POST /api/v1/doctors/{doctorId}/toggle-status`

**Example:** `POST /api/v1/doctors/DOC001/toggle-status`

**Expected Response:**
- Status: `200 OK`
- isActive toggled

**Test Flow:**
1. Get doctor, check isActive
2. Toggle status
3. Get doctor, verify isActive changed
4. Toggle again, verify original state restored

**Validation Tests:**
- [ ] Status code is 200
- [ ] isActive toggled
- [ ] Can toggle multiple times

---

### Scenario 13: Search Doctors
**Endpoint:** `GET /api/v1/doctors/search?searchTerm={term}`

**Examples:**
- `?searchTerm=john`
- `?searchTerm=cardiology`
- `?searchTerm=hospital.com`

**Expected Response:**
- Status: `200 OK`
- Results matching search term

**Validation Tests:**
- [ ] Results contain search term
- [ ] Pagination working
- [ ] Correct count

---

### Scenario 14: Get Doctor by Email
**Endpoint:** `GET /api/v1/doctors/email/{email}`

**Example:** `GET /api/v1/doctors/email/john.doe@hospital.com`

**Expected Response:**
- Status: `200 OK`
- Single doctor with that email

**Validation Tests:**
- [ ] Status code is 200
- [ ] Correct doctor returned
- [ ] Email matches

---

### Scenario 15: Check Doctor Exists by Email
**Endpoint:** `GET /api/v1/doctors/exists/email/{email}`

**Example:** `GET /api/v1/doctors/exists/email/john.doe@hospital.com`

**Expected Response (Exists):**
- Status: `200 OK`
- data: true

**Expected Response (Not Exists):**
- Status: `200 OK`
- data: false

**Validation Tests:**
- [ ] Returns true for existing email
- [ ] Returns false for non-existing email
- [ ] Status always 200

---

### Scenario 16: Get Active Doctors Count
**Endpoint:** `GET /api/v1/doctors/count/active`

**Expected Response:**
- Status: `200 OK`
- data contains integer count

**Example Response:**
```json
{
  "statusCode": 200,
  "message": "Count retrieved successfully",
  "data": 45,
  "timestamp": "2024-01-15T10:35:00"
}
```

**Validation Tests:**
- [ ] Status code is 200
- [ ] data is a number
- [ ] Count is accurate

---

## 🧬 Test Data Sets

### Test Data Set 1: Valid Doctor
```json
{
  "doctorId": "DOC001",
  "doctorName": "Dr. John Doe",
  "specialization": "Cardiology",
  "doctorEmail": "john.doe@email.com",
  "doctorPhone": "+1-234-567-8900",
  "joiningDate": "2020-01-15"
}
```

### Test Data Set 2: Invalid Email
```json
{
  "doctorName": "Dr. Test",
  "specialization": "Orthopedics",
  "doctorEmail": "invalid-email",
  "doctorPhone": "+1-234-567-8901",
  "joiningDate": "2020-01-15"
}
```

### Test Data Set 3: Short Name
```json
{
  "doctorName": "Dr",
  "specialization": "Neurology",
  "doctorEmail": "dr.test@email.com",
  "doctorPhone": "+1-234-567-8902",
  "joiningDate": "2020-01-15"
}
```

### Test Data Set 4: Future Date
```json
{
  "doctorName": "Dr. Future",
  "specialization": "Surgery",
  "doctorEmail": "future@email.com",
  "doctorPhone": "+1-234-567-8903",
  "joiningDate": "2025-12-31"
}
```

### Test Data Set 5: Short Phone
```json
{
  "doctorName": "Dr. Phone",
  "specialization": "Pediatrics",
  "doctorEmail": "phone@email.com",
  "doctorPhone": "123",
  "joiningDate": "2020-01-15"
}
```

---

## 📊 Test Coverage Checklist

### Happy Path Tests
- [x] Create valid doctor
- [x] Read doctor by ID
- [x] Get all doctors
- [x] Update doctor
- [x] Partial update
- [x] Delete doctor
- [x] Search doctors
- [x] Filter by specialization

### Error Path Tests
- [x] Duplicate email validation
- [x] Duplicate phone validation
- [x] Invalid email format
- [x] Short name validation
- [x] Future date validation
- [x] Short phone validation
- [x] Get non-existent doctor
- [x] Update non-existent doctor
- [x] Delete non-existent doctor

### Business Logic Tests
- [x] Toggle active status
- [x] Search with pagination
- [x] Count active doctors
- [x] Filter by specialization
- [x] Get by email

### Performance Tests
- [ ] Load test with 1000 doctors
- [ ] Pagination performance
- [ ] Search performance
- [ ] Sort performance

---

## 🚀 Running Tests

### Unit Tests
```bash
./gradlew test
```

### Specific Test Class
```bash
./gradlew test --tests DoctorServiceTest
./gradlew test --tests DoctorControllerTest
```

### With Coverage
```bash
./gradlew test jacocoTestReport
```

### Integration Tests
```bash
./gradlew integrationTest
```

---

## 📈 Performance Benchmarks

Expected Response Times (95th percentile):
- Read operations: < 100ms
- Write operations: < 200ms
- Search operations: < 300ms
- List operations (paginated): < 200ms

---

## 🔒 Security Testing

### Test Cases
- [ ] SQL injection attempts
- [ ] XSS payload in fields
- [ ] Authorization checks
- [ ] Rate limiting (if configured)
- [ ] CORS validation

### Example SQL Injection Test
```
Email: " OR "1"="1
Should be rejected by validation
```

---

## 🐛 Debugging Tips

### Enable Request/Response Logging
```properties
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

### Check Database
```sql
SELECT * FROM doctors;
SELECT COUNT(*) FROM doctors;
```

### API Response Inspection
- Check statusCode in response
- Verify message field
- Examine data payload
- Look for error field

---

## ✅ Test Sign-Off Checklist

- [ ] All CRUD operations working
- [ ] Validation passing correctly
- [ ] Exception handling working
- [ ] Pagination functional
- [ ] Search working
- [ ] Filter working
- [ ] Status toggle working
- [ ] Response format consistent
- [ ] HTTP status codes correct
- [ ] Performance acceptable

---

## 📞 Troubleshooting

### Common Issues

**Connection Refused**
- Verify MySQL is running
- Check connection string
- Verify port 3306 open

**Validation Errors**
- Check field lengths
- Verify email format
- Ensure future dates not used

**404 Not Found**
- Verify doctorId exists
- Check spelling
- Verify correct endpoint

**409 Conflict**
- Email already exists
- Phone already exists
- Check existing records

---

**Last Updated:** January 2024
**Test Framework:** JUnit 5, Mockito
**Status:** Ready for Production Testing
