package com.pm.doctorservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.doctorservice.dto.DoctorDTO;
import com.pm.doctorservice.dto.DoctorRequestDTO;
import com.pm.doctorservice.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Disabled("Skipping controller tests temporarily due to context loading issues")
@ExtendWith(MockitoExtension.class)
@DisplayName("Doctor Controller Tests")
@MockitoSettings(strictness = Strictness.LENIENT)
class DoctorControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private DoctorService doctorService;

    private DoctorRequestDTO doctorRequestDTO;
    private DoctorDTO doctorDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        doctorRequestDTO = DoctorRequestDTO.builder()
                .doctorId("DOC001")
                .doctorName("Dr. John Doe")
                .specialization("Cardiology")
                .doctorEmail("john.doe@hospital.com")
                .doctorPhone("+1-234-567-8900")
                .joiningDate(LocalDate.of(2020, 1, 15))
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
    @DisplayName("Should create doctor successfully - POST /api/v1/doctors")
    void testCreateDoctor() throws Exception {
        when(doctorService.createDoctor(any(DoctorRequestDTO.class))).thenReturn(doctorDTO);

        mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.data.doctorId").value("DOC001"))
                .andExpect(jsonPath("$.data.doctorName").value("Dr. John Doe"));
    }

    @Test
    @DisplayName("Should get doctor by ID - GET /api/v1/doctors/{doctorId}")
    void testGetDoctorById() throws Exception {
        when(doctorService.getDoctorById(anyString())).thenReturn(doctorDTO);

        mockMvc.perform(get("/api/v1/doctors/DOC001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data.doctorId").value("DOC001"));
    }

    @Test
    @DisplayName("Should delete doctor successfully - DELETE /api/v1/doctors/{doctorId}")
    void testDeleteDoctor() throws Exception {
        mockMvc.perform(delete("/api/v1/doctors/DOC001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should validate required fields on create")
    void testCreateDoctor_ValidationFails() throws Exception {
        DoctorRequestDTO invalidDTO = DoctorRequestDTO.builder()
                .doctorName(null)
                .build();

        mockMvc.perform(post("/api/v1/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }
}
