package com.pm.patientservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientResponse
{
    private String id;
    private String name;
    private String email;
    private String address;
    private LocalDate dateofBirth;
    private LocalDate registeredDate;
}
