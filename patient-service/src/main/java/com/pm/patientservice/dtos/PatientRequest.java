package com.pm.patientservice.dtos;

import com.pm.patientservice.dtos.validators.CreatePatientValidator;
import com.pm.patientservice.dtos.validators.UpdatePatientValidator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRequest
{
    @NotBlank(message = "Name is required",groups = {CreatePatientValidator.class, UpdatePatientValidator.class})
    @Size(max = 50, message = "Max size allowed is 50")
    private String name;

    @NotBlank(message = "Name is required" ,groups = CreatePatientValidator.class)
    private String email;

    @NotBlank(message = "Name is required",groups = {CreatePatientValidator.class, UpdatePatientValidator.class})
    private String address;

    @NotNull(groups = CreatePatientValidator.class)
    private LocalDate dateofBirth;

    @NotNull(groups = CreatePatientValidator.class)
    private LocalDate registeredDate;
}
