package com.pm.doctorservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Doctor Request DTO for create and update operations")
public class DoctorRequestDTO {

    @Schema(description = "Unique doctor ID (optional for create, required for update)", example = "DOC001")
    private String doctorId;

    @NotBlank(message = "Doctor name cannot be blank")
    @Size(min = 3, max = 100, message = "Doctor name must be between 3 and 100 characters")
    @Schema(description = "Full name of the doctor", example = "Dr. John Doe")
    private String doctorName;

    @NotBlank(message = "Specialization cannot be blank")
    @Size(min = 3, max = 50, message = "Specialization must be between 3 and 50 characters")
    @Schema(description = "Medical specialization", example = "Cardiology")
    private String specialization;

    @NotBlank(message = "Doctor email cannot be blank")
    @Email(message = "Email should be valid")
    @Schema(description = "Doctor's professional email", example = "john.doe@hospital.com")
    private String doctorEmail;

    @NotBlank(message = "Doctor phone cannot be blank")
    @Pattern(regexp = "^[\\d+\\-\\s()]{10,}$", message = "Phone number must be at least 10 digits")
    @Schema(description = "Doctor's contact number", example = "+1-234-567-8900")
    private String doctorPhone;

    @NotNull(message = "Joining date cannot be null")
    @PastOrPresent(message = "Joining date cannot be in the future")
    @Schema(description = "Doctor's joining date", example = "2020-01-15")
    private LocalDate joiningDate;
}
