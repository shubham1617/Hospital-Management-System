package com.pm.doctorservice.util;

import com.pm.doctorservice.dto.DoctorDTO;
import com.pm.doctorservice.dto.DoctorRequestDTO;
import com.pm.doctorservice.model.Doctor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorMapper {

    /**
     * Map Doctor entity to DoctorDTO
     */
    public DoctorDTO toDTO(Doctor doctor) {
        if (doctor == null) {
            return null;
        }

        return DoctorDTO.builder()
                .doctorId(doctor.getDoctorId())
                .doctorName(doctor.getDoctorName())
                .specialization(doctor.getSpecialization())
                .doctorEmail(doctor.getDoctorEmail())
                .doctorPhone(doctor.getDoctorPhone())
                .joiningDate(doctor.getJoiningDate())
                .isActive(doctor.getIsActive())
                .build();
    }

    /**
     * Map DoctorRequestDTO to Doctor entity
     */
    public Doctor toEntity(DoctorRequestDTO doctorRequestDTO) {
        if (doctorRequestDTO == null) {
            return null;
        }

        return Doctor.builder()
                .doctorId(doctorRequestDTO.getDoctorId())
                .doctorName(doctorRequestDTO.getDoctorName())
                .specialization(doctorRequestDTO.getSpecialization())
                .doctorEmail(doctorRequestDTO.getDoctorEmail())
                .doctorPhone(doctorRequestDTO.getDoctorPhone())
                .joiningDate(doctorRequestDTO.getJoiningDate())
                .build();
    }

    /**
     * Update Doctor entity from DoctorRequestDTO
     */
    public Doctor updateEntity(Doctor doctor, DoctorRequestDTO doctorRequestDTO) {
        if (doctor == null || doctorRequestDTO == null) {
            return null;
        }

        doctor.setDoctorName(doctorRequestDTO.getDoctorName());
        doctor.setSpecialization(doctorRequestDTO.getSpecialization());
        doctor.setDoctorEmail(doctorRequestDTO.getDoctorEmail());
        doctor.setDoctorPhone(doctorRequestDTO.getDoctorPhone());
        doctor.setJoiningDate(doctorRequestDTO.getJoiningDate());

        return doctor;
    }
}
