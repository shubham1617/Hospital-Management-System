# Doctor Service - Deployment & Production Guidelines

## 🚀 Deployment Checklist

### Pre-Deployment
- [ ] All unit tests pass: `./gradlew test`
- [ ] Code review completed
- [ ] Application builds successfully: `./gradlew build`
- [ ] No warnings or critical issues in logs
- [ ] Database schema is up to date
- [ ] Configuration files reviewed and validated
- [ ] Security scan completed
- [ ] Performance testing done

### Build Artifact
```bash
# Create executable JAR
./gradlew build

# Artifact location
build/libs/doctor-service-0.0.1-SNAPSHOT.jar
```

## 🔄 Deployment Strategies

### 1. Standalone JAR Deployment

```bash
# Run directly
java -jar doctor-service-0.0.1-SNAPSHOT.jar

# With custom configuration
java -Dspring.config.location=classpath:/application-prod.properties \
     -jar doctor-service-0.0.1-SNAPSHOT.jar

# With environment variables
java -Dspring.datasource.url=$DB_URL \
     -Dspring.datasource.username=$DB_USER \
     -Dspring.datasource.password=$DB_PASS \
     -jar doctor-service-0.0.1-SNAPSHOT.jar
```

### 2. Docker Deployment

#### Dockerfile
```dockerfile
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY build/libs/doctor-service-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Build Docker Image
```bash
docker build -t doctor-service:1.0.0 .
```

#### Run Docker Container
```bash
docker run -d \
  --name doctor-service \
  -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/doctor_db \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  doctor-service:1.0.0
```

#### Docker Compose
```yaml
version: '3.9'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: doctor_db
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  doctor-service:
    build: .
    port:
      - "8081:8081"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/doctor_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    depends_on:
      - mysql

volumes:
  mysql_data:
```

### 3. Kubernetes Deployment

#### Deployment YAML
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: doctor-service
  labels:
    app: doctor-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: doctor-service
  template:
    metadata:
      labels:
        app: doctor-service
    spec:
      containers:
      - name: doctor-service
        image: docker.io/your-registry/doctor-service:1.0.0
        ports:
        - containerPort: 8081
        env:
        - name: SPRING_DATASOURCE_URL
          valueFrom:
            configMapKeyRef:
              name: doctor-service-config
              key: db-url
        livenessProbe:
          httpGet:
            path: /doctor-service/actuator/health
            port: 8081
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /doctor-service/actuator/health
            port: 8081
          initialDelaySeconds: 5
          periodSeconds: 5
```

## 📋 Production Configuration Profiles

### Development (application-dev.properties)
```properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
logging.level.com.pm.doctorservice=DEBUG
```

### Staging (application-staging.properties)
```properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
logging.level.com.pm.doctorservice=INFO
```

### Production (application-prod.properties)
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.com.pm.doctorservice=WARN
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=10
```

## 🔒 Security in Production

### 1. Database Security
```properties
# Use strong password
spring.datasource.password=${DB_SECURE_PASSWORD}

# Use SSL for database connection
spring.datasource.url=jdbc:mysql://db-host:3306/doctor_db?useSSL=true&serverTimezone=UTC
```

### 2. Application Security
```properties
# Enable CORS selectively - not '*'
# In CorsConfig.java:
configuration.setAllowedOrigins(Arrays.asList("https://your-domain.com"));

# Use HTTPS
server.ssl.key-store=${KEYSTORE_PATH}
server.ssl.key-store-password=${KEYSTORE_PASSWORD}
```

### 3. Disable Swagger in Production
```properties
# application-prod.properties
springdoc.swagger-ui.enabled=false
```

## 📊 Monitoring & Health Checks

### Add Actuator Dependency
```gradle
implementation 'org.springframework.boot:spring-boot-starter-actuator'
```

### Configure Actuator Endpoints
```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
```

### Health Check URL
```
http://localhost:8081/doctor-service/actuator/health
```

## 📈 Performance Optimization

### 1. Database Indexing
Already configured in Doctor model for:
- doctor_email (unique)
- doctor_phone
- specialization
- is_active
- created_at

### 2. Connection Pooling
```properties
# Configure in application.properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.connection-timeout=20000
```

### 3. Caching (Optional)
Add Spring Cache annotations:
```java
@Cacheable(value = "doctors", key = "#doctorId")
public DoctorDTO getDoctorById(String doctorId)
```

## 🔄 Rolling Deployment

### Blue-Green Deployment
1. Deploy new version to green environment
2. Run smoke tests on green
3. Switch traffic from blue to green
4. Keep blue as rollback option

### Canary Deployment
```yaml
# Route 10% traffic to new version
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
metadata:
  name: doctor-service
spec:
  hosts:
  - doctor-service
  http:
  - match:
    - uri:
        prefix: /api
    route:
    - destination:
        host: doctor-service
        subset: v1
      weight: 90
    - destination:
        host: doctor-service
        subset: v2
      weight: 10
```

## 🔍 Logging & Monitoring

### Log Aggregation
```properties
# Configure logging pattern
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

### Send Logs to Central System
```gradle
implementation 'org.springframework.cloud:spring-cloud-starter-config'
implementation 'org.springframework.cloud:spring-cloud-starter-sleuth'
```

## 🧪 Smoke Tests

### Health Check
```bash
curl http://localhost:8081/doctor-service/actuator/health
```

### API Functionality
```bash
# Create doctor
curl -X POST http://localhost:8081/doctor-service/api/v1/doctors \
  -H "Content-Type: application/json" \
  -d '{"doctorName": "Test", ...}'

# Verify response
HTTP/1.1 201 Created
```

## 📋 Pre-Production Checklist

### Code Quality
- [ ] Sonar scan passed
- [ ] Code coverage > 80%
- [ ] No critical vulnerabilities

### Performance
- [ ] Load test completed
- [ ] Response time < 500ms for 95th percentile
- [ ] Memory usage stable under load

### Security
- [ ] Penetration testing passed
- [ ] SQL injection prevention verified
- [ ] CORS configuration reviewed
- [ ] Secrets externalized

### Reliability
- [ ] Failover testing passed
- [ ] Database backup verified
- [ ] Disaster recovery plan documented

## 🔄 Rollback Strategy

### Version Management
```bash
# Tag releases
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

### Rollback Process
```bash
# If new deployment has issues:
1. Identify problematic version
2. Redeploy previous stable version
3. Investigate root cause
4. Fix and re-deploy
```

## 📞 Incident Management

### Alert Configuration
```properties
# Email alerts for errors
spring.mail.host=${MAIL_HOST}
spring.mail.port=${MAIL_PORT}
spring.mail.username=${MAIL_USER}
spring.mail.password=${MAIL_PASS}
```

### On-Call Runbook
1. Check application logs
2. Verify database connectivity
3. Monitor resource usage
4. Check recent deployments
5. Execute rollback if necessary

## 📝 Post-Deployment Verification

- [ ] All endpoints responding with 200/201
- [ ] Database connectivity working
- [ ] Validation errors returning proper HTTP 400
- [ ] Not found errors returning HTTP 404
- [ ] Swagger UI accessible
- [ ] Logs showing normal operation
- [ ] Health check endpoint working
- [ ] Performance metrics normal

## 🚨 Common Issues & Solutions

### Issue: Out of Memory
```
Solution: Increase JVM heap size
java -Xmx2048m -jar doctor-service.jar
```

### Issue: Database Connection Pool Exhausted
```properties
# Increase pool size
spring.datasource.hikari.maximum-pool-size=30
```

### Issue: Slow Queries
```sql
-- Add indexes if missing
ALTER TABLE doctors ADD INDEX idx_doctor_email (doctor_email);
ALTER TABLE doctors ADD INDEX idx_specialization (specialization);
```

## 📞 Support & Contact

- **DevOps:** devops@company.com
- **Security:** security@company.com
- **DBA:** dba@company.com

---

**Last Updated:** January 2024
**Deployment Version:** 1.0.0
