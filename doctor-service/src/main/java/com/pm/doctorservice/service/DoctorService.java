package com.pm.doctorservice.service;

import com.pm.doctorservice.dto.DoctorDTO;
import com.pm.doctorservice.dto.DoctorRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DoctorService {

    /**
     * Create a new doctor
     */
    DoctorDTO createDoctor(DoctorRequestDTO doctorRequestDTO);

    /**
     * Get doctor by ID
     */
    DoctorDTO getDoctorById(String doctorId);

    /**
     * Get all doctors
     */
    List<DoctorDTO> getAllDoctors();

    /**
     * Get all doctors with pagination
     */
    Page<DoctorDTO> getAllDoctorsPaginated(Pageable pageable);

    /**
     * Get doctors by specialization
     */
    List<DoctorDTO> getDoctorsBySpecialization(String specialization);

    /**
     * Get active doctors by specialization with pagination
     */
    Page<DoctorDTO> getActiveDoctorsBySpecialization(String specialization, Pageable pageable);

    /**
     * Update doctor
     */
    DoctorDTO updateDoctor(String doctorId, DoctorRequestDTO doctorRequestDTO);

    /**
     * Partially update doctor
     */
    DoctorDTO partialUpdateDoctor(String doctorId, DoctorRequestDTO doctorRequestDTO);

    /**
     * Delete doctor
     */
    void deleteDoctor(String doctorId);

    /**
     * Toggle doctor status
     */
    DoctorDTO toggleDoctorStatus(String doctorId);

    /**
     * Get all active doctors
     */
    List<DoctorDTO> getActiveDoctors();

    /**
     * Get all active doctors with pagination
     */
    Page<DoctorDTO> getActiveDoctorsPaginated(Pageable pageable);

    /**
     * Get doctor by email
     */
    DoctorDTO getDoctorByEmail(String email);

    /**
     * Search doctors
     */
    Page<DoctorDTO> searchDoctors(String searchTerm, Pageable pageable);

    /**
     * Count active doctors
     */
    long countActiveDoctors();

    /**
     * Check if doctor exists by email
     */
    boolean doctorExistsByEmail(String email);

    /**
     * Check if doctor exists by phone
     */
    boolean doctorExistsByPhone(String phone);
}
