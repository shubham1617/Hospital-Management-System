package com.pm.appointmentservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pm.appointmentservice.dto.validation.CreateAppValidator;
import com.pm.appointmentservice.dto.validation.UpdateAppValidator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    
    private String appointmentId;
    @NotBlank(groups = CreateAppValidator.class, message = "feild cannot be blank")
    private String patientId;
    @NotBlank(groups = CreateAppValidator.class, message = "feild cannot be blank")
    private String doctorId;
    @NotNull(groups = {CreateAppValidator.class, UpdateAppValidator.class}, message = "feild cannot be blank")
    private LocalDate appointmentDate;
    @JsonFormat(pattern = "hh:mm a", locale = "en")
    @NotNull(groups ={CreateAppValidator.class, UpdateAppValidator.class}, message = "feild cannot be blank")
    private LocalTime appointmentTime;
    @NotBlank(groups = {CreateAppValidator.class, UpdateAppValidator.class}, message = "feild cannot be blank")
    private String status;
}