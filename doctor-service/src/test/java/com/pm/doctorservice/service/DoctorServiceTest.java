package com.pm.doctorservice.service;

import com.pm.doctorservice.dto.DoctorDTO;
import com.pm.doctorservice.dto.DoctorRequestDTO;
import com.pm.doctorservice.exception.DuplicateResourceException;
import com.pm.doctorservice.exception.ResourceNotFoundException;
import com.pm.doctorservice.model.Doctor;
import com.pm.doctorservice.repository.DoctorRepository;
import com.pm.doctorservice.service.impl.DoctorServiceImpl;
import com.pm.doctorservice.util.DoctorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Doctor Service Tests")
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private DoctorRequestDTO doctorRequestDTO;
    private Doctor doctor;
    private DoctorDTO doctorDTO;

    @BeforeEach
    void setUp() {
        // Initialize test data
        doctorRequestDTO = DoctorRequestDTO.builder()
                .doctorId("DOC001")
                .doctorName("Dr. John Doe")
                .specialization("Cardiology")
                .doctorEmail("john.doe@hospital.com")
                .doctorPhone("+1-234-567-8900")
                .joiningDate(LocalDate.of(2020, 1, 15))
                .build();

        doctor = Doctor.builder()
                .doctorId("DOC001")
                .doctorName("Dr. John Doe")
                .specialization("Cardiology")
                .doctorEmail("john.doe@hospital.com")
                .doctorPhone("+1-234-567-8900")
                .joiningDate(LocalDate.of(2020, 1, 15))
                .isActive(true)
                .build();

        doctorDTO = DoctorDTO.builder()
                .doctorId("DOC001")
                .doctorName("Dr. John Doe")
                .specialization("Cardiology")
                .doctorEmail("john.doe@hospital.com")
                .doctorPhone("+1-234-567-8900")
                .joiningDate(LocalDate.of(2020, 1, 15))
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should create doctor successfully")
    void testCreateDoctor_Success() {
        // Arrange
        when(doctorRepository.existsByDoctorEmail(anyString())).thenReturn(false);
        when(doctorRepository.existsByDoctorPhone(anyString())).thenReturn(false);
        when(doctorMapper.toEntity(any(DoctorRequestDTO.class))).thenReturn(doctor);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(doctorMapper.toDTO(any(Doctor.class))).thenReturn(doctorDTO);

        // Act
        DoctorDTO result = doctorService.createDoctor(doctorRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("DOC001", result.getDoctorId());
        assertEquals("Dr. John Doe", result.getDoctorName());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testCreateDoctor_DuplicateEmail() {
        // Arrange
        when(doctorRepository.existsByDoctorEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> doctorService.createDoctor(doctorRequestDTO));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should throw exception when phone already exists")
    void testCreateDoctor_DuplicatePhone() {
        // Arrange
        when(doctorRepository.existsByDoctorEmail(anyString())).thenReturn(false);
        when(doctorRepository.existsByDoctorPhone(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> doctorService.createDoctor(doctorRequestDTO));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should get doctor by ID successfully")
    void testGetDoctorById_Success() {
        // Arrange
        when(doctorRepository.findById(anyString())).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDTO(any(Doctor.class))).thenReturn(doctorDTO);

        // Act
        DoctorDTO result = doctorService.getDoctorById("DOC001");

        // Assert
        assertNotNull(result);
        assertEquals("DOC001", result.getDoctorId());
        assertEquals("Dr. John Doe", result.getDoctorName());
    }

    @Test
    @DisplayName("Should throw exception when doctor not found")
    void testGetDoctorById_NotFound() {
        // Arrange
        when(doctorRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> doctorService.getDoctorById("INVALID_ID"));
    }

    @Test
    @DisplayName("Should update doctor successfully")
    void testUpdateDoctor_Success() {
        // Arrange - Create a request with different email and phone to trigger duplicate checks
        DoctorRequestDTO updateRequest = DoctorRequestDTO.builder()
                .doctorId("DOC001")
                .doctorName("Dr. Jane Smith")
                .specialization("Neurology")
                .doctorEmail("jane.smith@hospital.com")
                .doctorPhone("+1-234-567-8901")
                .joiningDate(LocalDate.of(2021, 6, 20))
                .build();

        when(doctorRepository.findById(anyString())).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByDoctorEmail("jane.smith@hospital.com")).thenReturn(false);
        when(doctorRepository.existsByDoctorPhone("+1-234-567-8901")).thenReturn(false);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(doctorMapper.toDTO(any(Doctor.class))).thenReturn(doctorDTO);

        // Act
        DoctorDTO result = doctorService.updateDoctor("DOC001", updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("DOC001", result.getDoctorId());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should delete doctor successfully")
    void testDeleteDoctor_Success() {
        // Arrange
        when(doctorRepository.findById(anyString())).thenReturn(Optional.of(doctor));
        doNothing().when(doctorRepository).delete(any(Doctor.class));

        // Act
        doctorService.deleteDoctor("DOC001");

        // Assert
        verify(doctorRepository, times(1)).delete(any(Doctor.class));
    }

    @Test
    @DisplayName("Should toggle doctor status successfully")
    void testToggleDoctorStatus_Success() {
        // Arrange
        when(doctorRepository.findById(anyString())).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(doctorMapper.toDTO(any(Doctor.class))).thenReturn(doctorDTO);

        // Act
        DoctorDTO result = doctorService.toggleDoctorStatus("DOC001");

        // Assert
        assertNotNull(result);
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should check if doctor exists by email")
    void testDoctorExistsByEmail() {
        // Arrange
        when(doctorRepository.existsByDoctorEmail(anyString())).thenReturn(true);

        // Act
        boolean result = doctorService.doctorExistsByEmail("john.doe@hospital.com");

        // Assert
        assertTrue(result);
    }

    @Test
    @DisplayName("Should check if doctor exists by phone")
    void testDoctorExistsByPhone() {
        // Arrange
        when(doctorRepository.existsByDoctorPhone(anyString())).thenReturn(true);

        // Act
        boolean result = doctorService.doctorExistsByPhone("+1-234-567-8900");

        // Assert
        assertTrue(result);
    }
}
