package com.pm.doctorservice.controller;

import com.pm.doctorservice.dto.ApiResponse;
import com.pm.doctorservice.dto.DoctorDTO;
import com.pm.doctorservice.dto.DoctorRequestDTO;
import com.pm.doctorservice.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Doctor Management", description = "APIs for managing doctors")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DoctorController {

    private final DoctorService doctorService;

    /**
     * Create a new doctor
     */
    @PostMapping
    @Operation(summary = "Create a new doctor", description = "Create a new doctor with the provided details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Doctor created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Doctor already exists")
    })
    public ResponseEntity<ApiResponse<DoctorDTO>> createDoctor(@Valid @RequestBody DoctorRequestDTO doctorRequestDTO) 
    {
        log.info("Creating new doctor");
        DoctorDTO doctorDTO = doctorService.createDoctor(doctorRequestDTO);
        ApiResponse<DoctorDTO> response = ApiResponse.success(HttpStatus.CREATED.value(),"Doctor created successfully",doctorDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get doctor by ID
     */
    @GetMapping("/{doctorId}")
    @Operation(summary = "Get doctor by ID", description = "Retrieve doctor details by doctor ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctor retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    public ResponseEntity<ApiResponse<DoctorDTO>> getDoctorById(@PathVariable @Parameter(description = "Doctor ID") String doctorId) 
    {
        log.info("Fetching doctor with ID: {}", doctorId);
        DoctorDTO doctorDTO = doctorService.getDoctorById(doctorId);
        ApiResponse<DoctorDTO> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Doctor retrieved successfully",
                doctorDTO
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all doctors
     */
    @GetMapping
    @Operation(summary = "Get all doctors", description = "Retrieve all doctors with optional pagination")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctors retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<DoctorDTO>>> getAllDoctors(
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number (0-indexed)") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Page size") int size,
            @RequestParam(defaultValue = "doctorId") @Parameter(description = "Sort by field") String sortBy,
            @RequestParam(defaultValue = "ASC") @Parameter(description = "Sort direction") String direction) 
        {

        log.info("Fetching all doctors with pagination - page: {}, size: {}", page, size);
        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<DoctorDTO> doctorPage = doctorService.getAllDoctorsPaginated(pageable);

        ApiResponse<Page<DoctorDTO>> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Doctors retrieved successfully",
                doctorPage
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get doctors by specialization
     */
    @GetMapping("/specialization/{specialization}")
    @Operation(summary = "Get doctors by specialization", description = "Retrieve all doctors with a specific specialization")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctors retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No doctors found")
    })
    public ResponseEntity<ApiResponse<List<DoctorDTO>>> getDoctorsBySpecialization(
            @PathVariable @Parameter(description = "Medical specialization") String specialization) {
        log.info("Fetching doctors with specialization: {}", specialization);
        List<DoctorDTO> doctors = doctorService.getDoctorsBySpecialization(specialization);
        ApiResponse<List<DoctorDTO>> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Doctors retrieved successfully",
                doctors
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get active doctors by specialization with pagination
     */
    @GetMapping("/specialization/{specialization}/active")
    @Operation(summary = "Get active doctors by specialization", description = "Retrieve active doctors with a specific specialization")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctors retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<DoctorDTO>>> getActiveDoctorsBySpecialization(
            @PathVariable @Parameter(description = "Medical specialization") String specialization,
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number (0-indexed)") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Page size") int size) {
        log.info("Fetching active doctors by specialization: {}", specialization);

        Pageable pageable = PageRequest.of(page, size);
        Page<DoctorDTO> doctorPage = doctorService.getActiveDoctorsBySpecialization(specialization, pageable);

        ApiResponse<Page<DoctorDTO>> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Doctors retrieved successfully",
                doctorPage
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Update doctor
     */
    @PutMapping("/{doctorId}")
    @Operation(summary = "Update doctor", description = "Update complete doctor details")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctor updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Doctor already exists")
    })
    public ResponseEntity<ApiResponse<DoctorDTO>> updateDoctor(
            @PathVariable @Parameter(description = "Doctor ID") String doctorId,
            @Valid @RequestBody DoctorRequestDTO doctorRequestDTO) {
        log.info("Updating doctor with ID: {}", doctorId);
        DoctorDTO updatedDoctor = doctorService.updateDoctor(doctorId, doctorRequestDTO);
        ApiResponse<DoctorDTO> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Doctor updated successfully",
                updatedDoctor
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Partial update doctor
     */
    @PatchMapping("/{doctorId}")
    @Operation(summary = "Partially update doctor", description = "Update specific fields of a doctor")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctor updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    public ResponseEntity<ApiResponse<DoctorDTO>> partialUpdateDoctor(
            @PathVariable @Parameter(description = "Doctor ID") String doctorId,
            @RequestBody DoctorRequestDTO doctorRequestDTO) {
        log.info("Partially updating doctor with ID: {}", doctorId);
        DoctorDTO updatedDoctor = doctorService.partialUpdateDoctor(doctorId, doctorRequestDTO);
        ApiResponse<DoctorDTO> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Doctor updated successfully",
                updatedDoctor
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Delete doctor
     */
    @DeleteMapping("/{doctorId}")
    @Operation(summary = "Delete doctor", description = "Delete a doctor by ID")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Doctor deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    public ResponseEntity<Void> deleteDoctor(
            @PathVariable @Parameter(description = "Doctor ID") String doctorId) {
        log.info("Deleting doctor with ID: {}", doctorId);
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Toggle doctor status
     */
    @PostMapping("/{doctorId}/toggle-status")
    @Operation(summary = "Toggle doctor status", description = "Toggle active/inactive status of a doctor")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctor status toggled successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    public ResponseEntity<ApiResponse<DoctorDTO>> toggleDoctorStatus(
            @PathVariable @Parameter(description = "Doctor ID") String doctorId) {
        log.info("Toggling status for doctor with ID: {}", doctorId);
        DoctorDTO updatedDoctor = doctorService.toggleDoctorStatus(doctorId);
        ApiResponse<DoctorDTO> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Doctor status toggled successfully",
                updatedDoctor
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all active doctors
     */
    @GetMapping("/active")
    @Operation(summary = "Get active doctors", description = "Retrieve all active doctors with pagination")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctors retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<DoctorDTO>>> getActiveDoctors(
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number (0-indexed)") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Page size") int size) {
        log.info("Fetching active doctors with pagination - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<DoctorDTO> doctorPage = doctorService.getActiveDoctorsPaginated(pageable);

        ApiResponse<Page<DoctorDTO>> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Doctors retrieved successfully",
                doctorPage
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get doctor by email
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Get doctor by email", description = "Retrieve doctor details by email address")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctor retrieved successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    public ResponseEntity<ApiResponse<DoctorDTO>> getDoctorByEmail(
            @PathVariable @Parameter(description = "Doctor email") String email) {
        log.info("Fetching doctor by email: {}", email);
        DoctorDTO doctorDTO = doctorService.getDoctorByEmail(email);
        ApiResponse<DoctorDTO> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Doctor retrieved successfully",
                doctorDTO
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Search doctors
     */
    @GetMapping("/search")
    @Operation(summary = "Search doctors", description = "Search doctors by name, email, or specialization")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Doctors retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Page<DoctorDTO>>> searchDoctors(
            @RequestParam @Parameter(description = "Search term") String searchTerm,
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number (0-indexed)") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Page size") int size) {
        log.info("Searching doctors with term: {}", searchTerm);

        Pageable pageable = PageRequest.of(page, size);
        Page<DoctorDTO> doctorPage = doctorService.searchDoctors(searchTerm, pageable);

        ApiResponse<Page<DoctorDTO>> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Doctors retrieved successfully",
                doctorPage
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get count of active doctors
     */
    @GetMapping("/count/active")
    @Operation(summary = "Get active doctors count", description = "Retrieve the count of active doctors")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    })
    public ResponseEntity<ApiResponse<Long>> getActiveDoctorsCount() {
        log.info("Fetching active doctors count");
        long count = doctorService.countActiveDoctors();
        ApiResponse<Long> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Count retrieved successfully",
                count
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Check if doctor exists by email
     */
    @GetMapping("/exists/email/{email}")
    @Operation(summary = "Check doctor by email", description = "Check if a doctor exists with the given email")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    public ResponseEntity<ApiResponse<Boolean>> doctorExistsByEmail(
            @PathVariable @Parameter(description = "Doctor email") String email) {
        log.info("Checking if doctor exists by email: {}", email);
        boolean exists = doctorService.doctorExistsByEmail(email);
        ApiResponse<Boolean> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Check completed successfully",
                exists
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Check if doctor exists by phone
     */
    @GetMapping("/exists/phone/{phone}")
    @Operation(summary = "Check doctor by phone", description = "Check if a doctor exists with the given phone number")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Check completed successfully")
    })
    public ResponseEntity<ApiResponse<Boolean>> doctorExistsByPhone(
            @PathVariable @Parameter(description = "Doctor phone") String phone) {
        log.info("Checking if doctor exists by phone: {}", phone);
        boolean exists = doctorService.doctorExistsByPhone(phone);
        ApiResponse<Boolean> response = ApiResponse.success(
                HttpStatus.OK.value(),
                "Check completed successfully",
                exists
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
