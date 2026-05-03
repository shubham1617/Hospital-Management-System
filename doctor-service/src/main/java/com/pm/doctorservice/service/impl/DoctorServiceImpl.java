package com.pm.doctorservice.service.impl;

import com.pm.doctorservice.dto.DoctorDTO;
import com.pm.doctorservice.dto.DoctorRequestDTO;
import com.pm.doctorservice.exception.DuplicateResourceException;
import com.pm.doctorservice.exception.InvalidInputException;
import com.pm.doctorservice.exception.ResourceNotFoundException;
import com.pm.doctorservice.model.Doctor;
import com.pm.doctorservice.repository.DoctorRepository;
import com.pm.doctorservice.service.DoctorService;
import com.pm.doctorservice.util.DoctorMapper;
import com.pm.doctorservice.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final IdGenerator idGenerator;

    @Override
    public DoctorDTO createDoctor(DoctorRequestDTO doctorRequestDTO) {
        log.info("Creating new doctor with email: {}", doctorRequestDTO.getDoctorEmail());

        // Check for duplicate email
        if (doctorRepository.existsByDoctorEmail(doctorRequestDTO.getDoctorEmail())) {
            throw new DuplicateResourceException("Doctor", "email", doctorRequestDTO.getDoctorEmail());
        }

        // Check for duplicate phone
        if (doctorRepository.existsByDoctorPhone(doctorRequestDTO.getDoctorPhone())) {
            throw new DuplicateResourceException("Doctor", "phone", doctorRequestDTO.getDoctorPhone());
        }

        // Generate doctor ID if not provided
        String doctorId = doctorRequestDTO.getDoctorId();
        if (doctorId == null || doctorId.isBlank()) {
            doctorId = idGenerator.generateDoctorId();
        }

        // Map DTO to entity
        Doctor doctor = doctorMapper.toEntity(doctorRequestDTO);
        doctor.setDoctorId(doctorId);
        doctor.setIsActive(true);

        // Save to database
        Doctor savedDoctor = doctorRepository.save(doctor);
        log.info("Doctor created successfully with ID: {}", savedDoctor.getDoctorId());

        return doctorMapper.toDTO(savedDoctor);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDTO getDoctorById(String doctorId) {
        log.info("Fetching doctor with ID: {}", doctorId);

        if (doctorId == null || doctorId.isBlank()) {
            throw new InvalidInputException("Doctor ID cannot be null or empty");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));

        return doctorMapper.toDTO(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDTO> getAllDoctors() {
        log.info("Fetching all doctors");

        return doctorRepository.findAll().stream()
                .map(doctorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DoctorDTO> getAllDoctorsPaginated(Pageable pageable) {
        log.info("Fetching all doctors with pagination: {}", pageable);

        return doctorRepository.findAll(pageable)
                .map(doctorMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDTO> getDoctorsBySpecialization(String specialization) {
        log.info("Fetching doctors by specialization: {}", specialization);

        if (specialization == null || specialization.isBlank()) {
            throw new InvalidInputException("Specialization cannot be null or empty");
        }

        return doctorRepository.findBySpecialization(specialization).stream()
                .map(doctorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DoctorDTO> getActiveDoctorsBySpecialization(String specialization, Pageable pageable) {
        log.info("Fetching active doctors by specialization: {}", specialization);

        if (specialization == null || specialization.isBlank()) {
            throw new InvalidInputException("Specialization cannot be null or empty");
        }

        return doctorRepository.findActiveBySpecialization(specialization, pageable)
                .map(doctorMapper::toDTO);
    }

    @Override
    public DoctorDTO updateDoctor(String doctorId, DoctorRequestDTO doctorRequestDTO) {
        log.info("Updating doctor with ID: {}", doctorId);

        if (doctorId == null || doctorId.isBlank()) {
            throw new InvalidInputException("Doctor ID cannot be null or empty");
        }

        if (doctorRequestDTO == null) {
            throw new InvalidInputException("Doctor request cannot be null");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));

        // Check for duplicate email if email is being changed
        if (!doctor.getDoctorEmail().equals(doctorRequestDTO.getDoctorEmail()) &&
                doctorRepository.existsByDoctorEmail(doctorRequestDTO.getDoctorEmail())) {
            throw new DuplicateResourceException("Doctor", "email", doctorRequestDTO.getDoctorEmail());
        }

        // Check for duplicate phone if phone is being changed
        if (!doctor.getDoctorPhone().equals(doctorRequestDTO.getDoctorPhone()) &&
                doctorRepository.existsByDoctorPhone(doctorRequestDTO.getDoctorPhone())) {
            throw new DuplicateResourceException("Doctor", "phone", doctorRequestDTO.getDoctorPhone());
        }

        // Update fields
        doctor.setDoctorName(doctorRequestDTO.getDoctorName());
        doctor.setSpecialization(doctorRequestDTO.getSpecialization());
        doctor.setDoctorEmail(doctorRequestDTO.getDoctorEmail());
        doctor.setDoctorPhone(doctorRequestDTO.getDoctorPhone());
        doctor.setJoiningDate(doctorRequestDTO.getJoiningDate());

        Doctor updatedDoctor = doctorRepository.save(doctor);
        log.info("Doctor updated successfully with ID: {}", updatedDoctor.getDoctorId());

        return doctorMapper.toDTO(updatedDoctor);
    }

    @Override
    public DoctorDTO partialUpdateDoctor(String doctorId, DoctorRequestDTO doctorRequestDTO) {
        log.info("Partially updating doctor with ID: {}", doctorId);

        if (doctorId == null || doctorId.isBlank()) {
            throw new InvalidInputException("Doctor ID cannot be null or empty");
        }

        if (doctorRequestDTO == null) {
            throw new InvalidInputException("Doctor request cannot be null");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));

        // Update only non-null fields
        if (doctorRequestDTO.getDoctorName() != null && !doctorRequestDTO.getDoctorName().isBlank()) {
            doctor.setDoctorName(doctorRequestDTO.getDoctorName());
        }

        if (doctorRequestDTO.getSpecialization() != null && !doctorRequestDTO.getSpecialization().isBlank()) {
            doctor.setSpecialization(doctorRequestDTO.getSpecialization());
        }

        if (doctorRequestDTO.getDoctorEmail() != null && !doctorRequestDTO.getDoctorEmail().isBlank()) {
            if (!doctor.getDoctorEmail().equals(doctorRequestDTO.getDoctorEmail()) &&
                    doctorRepository.existsByDoctorEmail(doctorRequestDTO.getDoctorEmail())) {
                throw new DuplicateResourceException("Doctor", "email", doctorRequestDTO.getDoctorEmail());
            }
            doctor.setDoctorEmail(doctorRequestDTO.getDoctorEmail());
        }

        if (doctorRequestDTO.getDoctorPhone() != null && !doctorRequestDTO.getDoctorPhone().isBlank()) {
            if (!doctor.getDoctorPhone().equals(doctorRequestDTO.getDoctorPhone()) &&
                    doctorRepository.existsByDoctorPhone(doctorRequestDTO.getDoctorPhone())) {
                throw new DuplicateResourceException("Doctor", "phone", doctorRequestDTO.getDoctorPhone());
            }
            doctor.setDoctorPhone(doctorRequestDTO.getDoctorPhone());
        }

        if (doctorRequestDTO.getJoiningDate() != null) {
            doctor.setJoiningDate(doctorRequestDTO.getJoiningDate());
        }

        Doctor updatedDoctor = doctorRepository.save(doctor);
        log.info("Doctor partially updated successfully with ID: {}", updatedDoctor.getDoctorId());

        return doctorMapper.toDTO(updatedDoctor);
    }

    @Override
    public void deleteDoctor(String doctorId) {
        log.info("Deleting doctor with ID: {}", doctorId);

        if (doctorId == null || doctorId.isBlank()) {
            throw new InvalidInputException("Doctor ID cannot be null or empty");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));

        doctorRepository.delete(doctor);
        log.info("Doctor deleted successfully with ID: {}", doctorId);
    }

    @Override
    public DoctorDTO toggleDoctorStatus(String doctorId) {
        log.info("Toggling status for doctor with ID: {}", doctorId);

        if (doctorId == null || doctorId.isBlank()) {
            throw new InvalidInputException("Doctor ID cannot be null or empty");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", doctorId));

        doctor.setIsActive(!doctor.getIsActive());
        Doctor updatedDoctor = doctorRepository.save(doctor);
        log.info("Doctor status toggled successfully with ID: {}", updatedDoctor.getDoctorId());

        return doctorMapper.toDTO(updatedDoctor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorDTO> getActiveDoctors() {
        log.info("Fetching all active doctors");

        return doctorRepository.findByIsActiveTrue().stream()
                .map(doctorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DoctorDTO> getActiveDoctorsPaginated(Pageable pageable) {
        log.info("Fetching active doctors with pagination: {}", pageable);

        return doctorRepository.findByIsActiveTrue(pageable)
                .map(doctorMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public DoctorDTO getDoctorByEmail(String email) {
        log.info("Fetching doctor by email: {}", email);

        if (email == null || email.isBlank()) {
            throw new InvalidInputException("Email cannot be null or empty");
        }

        Doctor doctor = doctorRepository.findByDoctorEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "email", email));

        return doctorMapper.toDTO(doctor);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DoctorDTO> searchDoctors(String searchTerm, Pageable pageable) {
        log.info("Searching doctors with term: {}", searchTerm);

        if (searchTerm == null || searchTerm.isBlank()) {
            throw new InvalidInputException("Search term cannot be null or empty");
        }

        return doctorRepository.searchDoctors(searchTerm, pageable)
                .map(doctorMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveDoctors() {
        log.info("Counting active doctors");
        return doctorRepository.countByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean doctorExistsByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidInputException("Email cannot be null or empty");
        }
        return doctorRepository.existsByDoctorEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean doctorExistsByPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new InvalidInputException("Phone cannot be null or empty");
        }
        return doctorRepository.existsByDoctorPhone(phone);
    }
}
