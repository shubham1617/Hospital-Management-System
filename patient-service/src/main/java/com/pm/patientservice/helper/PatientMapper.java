package com.pm.patientservice.helper;

import com.pm.patientservice.dtos.PatientResponse;
import com.pm.patientservice.model.Patient;

public class PatientMapper
{
    public static PatientResponse entityToResponse(Patient entity)
    {
        PatientResponse response = new PatientResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setEmail(entity.getEmail());
        response.setAddress(entity.getAddress());
        response.setDateofBirth(entity.getDateofBirth());
        response.setRegisteredDate(entity.getRegisteredDate());
        return response;
    }

    public static Patient responseToEntity(PatientResponse entity)
    {
        Patient patient = new Patient();
        patient.setId(entity.getId());
        patient.setName(entity.getName());
        patient.setEmail(entity.getEmail());
        patient.setAddress(entity.getAddress());
        patient.setDateofBirth(entity.getDateofBirth());
        patient.setRegisteredDate(entity.getRegisteredDate());
        return patient;
    }
}
