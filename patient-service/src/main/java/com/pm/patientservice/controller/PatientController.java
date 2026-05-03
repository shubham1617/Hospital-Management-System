package com.pm.patientservice.controller;

import com.pm.patientservice.dtos.PatientRequest;
import com.pm.patientservice.dtos.PatientResponse;
import com.pm.patientservice.dtos.validators.CreatePatientValidator;
import com.pm.patientservice.dtos.validators.UpdatePatientValidator;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patient")
@Tag(name="Patient Service", description = "Endpoints for managing patients")
public class PatientController
{
    private final PatientService patientService;
    public PatientController(PatientService patientService)
    {
        this.patientService = patientService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Patient Details by ID")
    public PatientResponse getPatientById(@PathVariable String id)
    {
         return patientService.getPatientById(id);
    }

    @GetMapping("/findAll")
    @Operation(summary = "Get all patient details")
    public List<PatientResponse> getPatients()
    {
        return patientService.getAll();
    }

    @PostMapping("/add")
    @Operation(summary = "Add a new patient")
    public ResponseEntity<PatientResponse> createPatient( @Validated({CreatePatientValidator.class}) @RequestBody PatientRequest patientRequest)
    {
        return new ResponseEntity<>(patientService.addPatient(patientRequest), HttpStatus.CREATED);
    }

    @PutMapping(path = "/update/{id}")
    @Operation(summary = "Update patient details by ID")
    public PatientResponse updatePatient(@Validated({UpdatePatientValidator.class}) @RequestBody PatientRequest patientRequest, @PathVariable String id)
    {
        return patientService.updatePatient(id, patientRequest);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete patient by ID")
    public  ResponseEntity<String> deletePatient(@PathVariable String id)
    {
        patientService.deletePatient(id);
        return  new ResponseEntity<>( "Patient Deleted", HttpStatus.OK);
    }
}
