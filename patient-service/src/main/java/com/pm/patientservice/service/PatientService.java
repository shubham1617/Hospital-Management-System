package com.pm.patientservice.service;

import com.pm.patientservice.dtos.PatientRequest;
import com.pm.patientservice.dtos.PatientResponse;
import com.pm.patientservice.exception.EmailAlreadyExistException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.helper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService
{
    private final PatientRepo patientRepo;

    public PatientService(PatientRepo patientRepo)
    {
        this.patientRepo = patientRepo;
    }

    public PatientResponse getPatientById(String id)
    {
        Patient patient = patientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        PatientResponse patientResponse = PatientMapper.entityToResponse(patient);
        return patientResponse;
    }

    public  List<PatientResponse> getAll()
    {
        List<Patient> patients = patientRepo.findAll();

        //Using old school way
//        List<PatientResponse> patientResponseList = new ArrayList<>();
//        for (Patient patient : patients)
//        {
//            patientResponseList.add(PatientMapper.entityToResponse(patient));
//        }

        //Using Stream API
        List<PatientResponse> patientResponseList = patients.stream()
                .map(PatientMapper::entityToResponse).toList();
        return patientResponseList;
    }

    public PatientResponse addPatient(PatientRequest patientRequest)
    {
        if(patientRepo.existsByEmail(patientRequest.getEmail()))
        {
            throw new EmailAlreadyExistException("Patient already exists with email " + patientRequest.getEmail());
        }
        Patient patient = Patient.builder()
                .id(UUID.randomUUID().toString())
                .name(patientRequest.getName())
                .address(patientRequest.getAddress())
                .dateofBirth(patientRequest.getDateofBirth())
                .registeredDate(patientRequest.getRegisteredDate())
                .email(patientRequest.getEmail())
                .build();
        Patient savedPatient = patientRepo.save(patient);
        return PatientMapper.entityToResponse(savedPatient);

    }

    public PatientResponse updatePatient(String id, PatientRequest patientRequest)
    {
        Patient patient = patientRepo.findById(id).orElseThrow(() ->
                new PatientNotFoundException("Patient not found with Id " + id));

        if(patientRequest.getAddress() != null)
        {
            patient.setAddress(patientRequest.getAddress());
        }
        if(patientRequest.getName() != null)
        {
            patient.setName(patientRequest.getName());
        }
        if(patientRequest.getDateofBirth() != null && !patientRequest.getDateofBirth().equals(patient.getDateofBirth()))
        {
            throw new RuntimeException("DOB cannot be updated");
        }
        if(patientRequest.getRegisteredDate() != null && !patient.getRegisteredDate().equals(patientRequest.getRegisteredDate()))
        {
            throw new RuntimeException("Registered Date cannot be updated");
        }
        if(patientRequest.getEmail() != null &&  !patientRequest.getEmail().equals(patient.getEmail()))
        {
            throw new RuntimeException("Email cannot be updated");
        }
        patientRepo.save(patient);
        return PatientMapper.entityToResponse(patient);
    }

    public void deletePatient(String id)
    {
        Patient byId = patientRepo.findById(id).orElseThrow(
                () -> new RuntimeException("Patient Not Found !!")
        );
        patientRepo.deleteById(id);
    }
}
