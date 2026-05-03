package com.pm.doctorservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctors", indexes = {
        @Index(name = "idx_doctor_email", columnList = "doctorEmail", unique = true),
        @Index(name = "idx_doctor_phone", columnList = "doctorPhone")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @Column(name = "doctor_id", length = 50)
    private String doctorId;

    @NotBlank(message = "Doctor name cannot be blank")
    @Size(min = 3, max = 100, message = "Doctor name must be between 3 and 100 characters")
    @Column(name = "doctor_name", nullable = false, length = 100)
    private String doctorName;

    @NotBlank(message = "Specialization cannot be blank")
    @Size(min = 3, max = 50, message = "Specialization must be between 3 and 50 characters")
    @Column(name = "specialization", nullable = false, length = 50)
    private String specialization;

    @NotBlank(message = "Doctor email cannot be blank")
    @Email(message = "Email should be valid")
    @Column(name = "doctor_email", nullable = false, unique = true, length = 100)
    private String doctorEmail;

    @NotBlank(message = "Doctor phone cannot be blank")
    @Pattern(regexp = "^[\\d+\\-\\s()]{10,}$", message = "Phone number must be at least 10 digits")
    @Column(name = "doctor_phone", nullable = false, length = 20)
    private String doctorPhone;

    @NotNull(message = "Joining date cannot be null")
    @PastOrPresent(message = "Joining date cannot be in the future")
    @Column(name = "joining_date", nullable = false)
    private LocalDate joiningDate;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
