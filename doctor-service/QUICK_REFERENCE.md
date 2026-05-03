# Doctor Service - API Quick Reference Guide

## 🚀 Quick Start Commands

### Build and Run
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Run with specific profile
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## 📋 Common cURL Commands

### 1. Create a Doctor
```bash
curl -X POST http://localhost:8081/doctor-service/api/v1/doctors \
  -H "Content-Type: application/json" \
  -d '{
    "doctorId": "DOC001",
    "doctorName": "Dr. John Doe",
    "specialization": "Cardiology",
    "doctorEmail": "john.doe@hospital.com",
    "doctorPhone": "+1-234-567-8900",
    "joiningDate": "2020-01-15"
  }'
```

### 2. Get Doctor by ID
```bash
curl http://localhost:8081/doctor-service/api/v1/doctors/DOC001
```

### 3. Get All Doctors (Paginated)
```bash
curl "http://localhost:8081/doctor-service/api/v1/doctors?page=0&size=10&sortBy=doctorId&direction=ASC"
```

### 4. Update Doctor (Full)
```bash
curl -X PUT http://localhost:8081/doctor-service/api/v1/doctors/DOC001 \
  -H "Content-Type: application/json" \
  -d '{
    "doctorName": "Dr. Jane Smith",
    "specialization": "Neurology",
    "doctorEmail": "jane.smith@hospital.com",
    "doctorPhone": "+1-234-567-8910",
    "joiningDate": "2020-01-15"
  }'
```

### 5. Partial Update Doctor
```bash
curl -X PATCH http://localhost:8081/doctor-service/api/v1/doctors/DOC001 \
  -H "Content-Type: application/json" \
  -d '{
    "specialization": "Orthopedics"
  }'
```

### 6. Delete Doctor
```bash
curl -X DELETE http://localhost:8081/doctor-service/api/v1/doctors/DOC001
```

### 7. Get Doctors by Specialization
```bash
curl "http://localhost:8081/doctor-service/api/v1/doctors/specialization/Cardiology"
```

### 8. Get Active Doctors
```bash
curl "http://localhost:8081/doctor-service/api/v1/doctors/active?page=0&size=10"
```

### 9. Toggle Doctor Status
```bash
curl -X POST http://localhost:8081/doctor-service/api/v1/doctors/DOC001/toggle-status
```

### 10. Search Doctors
```bash
curl "http://localhost:8081/doctor-service/api/v1/doctors/search?searchTerm=john&page=0&size=10"
```

### 11. Get Doctor by Email
```bash
curl "http://localhost:8081/doctor-service/api/v1/doctors/email/john.doe@hospital.com"
```

### 12. Check if Doctor Exists by Email
```bash
curl "http://localhost:8081/doctor-service/api/v1/doctors/exists/email/john.doe@hospital.com"
```

### 13. Check if Doctor Exists by Phone
```bash
curl "http://localhost:8081/doctor-service/api/v1/doctors/exists/phone/%2B1-234-567-8900"
```

### 14. Get Active Doctors Count
```bash
curl "http://localhost:8081/doctor-service/api/v1/doctors/count/active"
```

## 🔍 Response Format

### Success Response
```json
{
  "statusCode": 200,
  "message": "Operation successful",
  "data": { },
  "timestamp": "2024-01-15T10:30:00"
}
```

### Error Response
```json
{
  "statusCode": 400,
  "message": "Validation failed",
  "error": "Doctor name cannot be blank",
  "timestamp": "2024-01-15T10:30:00"
}
```

## 📊 HTTP Status Codes

| Code | Meaning | Usage |
|------|---------|-------|
| 200 | OK | Successful GET, PUT, PATCH |
| 201 | Created | Successful POST |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Invalid input or validation error |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Duplicate resource (email/phone) |
| 500 | Server Error | Unexpected error |

## 📚 Database Connection

### MySQL Connection String
```
jdbc:mysql://localhost:3306/doctor_db
```

### Create Database Manually (if needed)
```sql
CREATE DATABASE doctor_db;
```

### SQL Query Examples

#### Get all doctors
```sql
SELECT * FROM doctors;
```

#### Get active doctors
```sql
SELECT * FROM doctors WHERE is_active = TRUE;
```

#### Get doctors by specialization
```sql
SELECT * FROM doctors WHERE specialization = 'Cardiology';
```

#### Count doctors
```sql
SELECT COUNT(*) FROM doctors;
```

#### Find doctor by email
```sql
SELECT * FROM doctors WHERE doctor_email = 'john.doe@hospital.com';
```

## 🔧 Troubleshooting

### Issue: Connection refused
```
Error: Connection refused
Solution: Ensure MySQL is running and credentials are correct in application.properties
```

### Issue: Port already in use
```
Error: Address already in use
Solution: Change server.port in application.properties or kill existing process
```

### Issue: Validation error
```
Error: Doctor name cannot be blank
Solution: Ensure all required fields are provided in request body
```

### Issue: Duplicate email/phone
```
Error: Doctor already exists with email
Solution: Use unique email and phone for new doctor or update existing doctor
```

## 🧮 Sample Test Data

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

## 📖 Swagger UI

**Local:** http://localhost:8081/doctor-service/swagger-ui.html

**API Docs:** http://localhost:8081/doctor-service/v3/api-docs

## 💡 Tips & Tricks

### Sorting
```bash
# Sort by doctor name in descending order
curl "http://localhost:8081/doctor-service/api/v1/doctors?sortBy=doctorName&direction=DESC"
```

### Pagination
```bash
# Get second page with 20 items per page
curl "http://localhost:8081/doctor-service/api/v1/doctors?page=1&size=20"
```

### Search with Multiple Specializations
Use the general search endpoint:
```bash
curl "http://localhost:8081/doctor-service/api/v1/doctors/search?searchTerm=cardiology"
```

### Date Format
All dates should be in ISO format: `YYYY-MM-DD`
```json
{
  "joiningDate": "2020-01-15"
}
```

## 🔐 Common Validation Rules

1. **Doctor Name**: 3-100 characters, cannot be blank
2. **Specialization**: 3-50 characters, cannot be blank
3. **Email**: Valid email format, cannot be blank, must be unique
4. **Phone**: 10+ digits, cannot be blank, must be unique
5. **Joining Date**: Cannot be in the future, cannot be null

## 📝 Log Levels

Control logging in `application.properties`:
```properties
# Debug level (most detailed)
logging.level.com.pm.doctorservice=DEBUG

# Info level (important messages only)
logging.level.com.pm.doctorservice=INFO

# Error level (only errors)
logging.level.com.pm.doctorservice=ERROR
```

---

**Need Help?** Check the COMPLETE_README.md for detailed documentation.
