package com.pm.appointmentservice.service;

import com.pm.appointmentservice.dto.ApiResponse;
import com.pm.appointmentservice.dto.AppointmentDTO;
import com.pm.appointmentservice.dto.DoctorDTO;
import com.pm.appointmentservice.dto.PatientResponse;
import com.pm.appointmentservice.model.Appointments;
import com.pm.appointmentservice.repository.AppointmentRepository;

import java.util.Optional;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient webClient;

    public ResponseEntity<AppointmentDTO> createAppointment(AppointmentDTO appointmentDTO) {
        // Fetch patient details from another API
        PatientResponse patient = fetchPatient(appointmentDTO.getPatientId());
        if (patient == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // Fetch doctor details from another API
        ApiResponse<DoctorDTO> doctor = fetchDoctor(appointmentDTO.getDoctorId());
        if (doctor == null) {
            return ResponseEntity.badRequest().body(null);
        }

        if(appointmentDTO.getAppointmentId() == null)
        {
            appointmentDTO.setAppointmentId(UUID.randomUUID().toString());
        }

        // Create appointment entity
        Appointments appointmentEntity = new Appointments(
                appointmentDTO.getAppointmentId(),
                appointmentDTO.getPatientId(),
                appointmentDTO.getDoctorId(),
                appointmentDTO.getAppointmentDate(),
                appointmentDTO.getAppointmentTime(),
                appointmentDTO.getStatus()
        );

        // Save the appointment to the database
        Appointments savedAppointment = appointmentRepository.save(appointmentEntity);

        // Return the created appointment as a DTO
        return ResponseEntity.ok(new AppointmentDTO(
                savedAppointment.getAppointmentId(),
                savedAppointment.getPatientId(),
                savedAppointment.getDoctorId(),
                savedAppointment.getAppointmentDate(),
                savedAppointment.getAppointmentTime(),
                savedAppointment.getStatus()
        ));
    }

    public ResponseEntity<AppointmentDTO> rescheduleAppointment(String id, AppointmentDTO appointmentDTO) {
        // Fetch the existing appointment
        Appointments existingAppointment = null;
        try {
            existingAppointment = appointmentRepository.findById(id).orElse(null);
        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException("Appointment Not Found");
        }
        
        // Update the appointment details
        if(existingAppointment.getDoctorId() != null && !existingAppointment.getDoctorId().equals(appointmentDTO.getDoctorId())){
            throw new RuntimeException("You cannot modify doctorId");
        }

        
        if(existingAppointment.getPatientId() != null && !existingAppointment.getPatientId().equals(appointmentDTO.getPatientId())){
            throw new RuntimeException("You cannot modify patientId");
        }

        
        if(existingAppointment.getAppointmentId() != null && appointmentDTO.getAppointmentId()!=null && !existingAppointment.getAppointmentId().equals(appointmentDTO.getAppointmentId())){
            throw new RuntimeException("You cannot modify appointmentId");
        }

        existingAppointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
        existingAppointment.setAppointmentTime(appointmentDTO.getAppointmentTime());
        existingAppointment.setStatus(appointmentDTO.getStatus());

        // Save the updated appointment to the database
        Appointments updatedAppointment = appointmentRepository.save(existingAppointment);

        // Return the updated appointment as a DTO
        return ResponseEntity.ok(new AppointmentDTO(
                updatedAppointment.getAppointmentId(),
                updatedAppointment.getPatientId(),
                updatedAppointment.getDoctorId(),
                updatedAppointment.getAppointmentDate(),
                updatedAppointment.getAppointmentTime(),
                updatedAppointment.getStatus()
        ));
    }

    public void deleteAppointment(String id)
    {
        appointmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment Not Found"));
        appointmentRepository.deleteById(id);

    }

   private ApiResponse<DoctorDTO> fetchDoctor(String doctorId) {
    ApiResponse<DoctorDTO> block = null;
    try {

         block = webClient.get()
            .uri("http://localhost:8083/doctor-service/api/v1/doctors/{id}", doctorId)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<ApiResponse<DoctorDTO>>() {})
            .block();
    } catch (Exception e) {
        // TODO: handle exception
        throw new RuntimeErrorException( null,"Doctor Not Found");
    }

    return block;
    
}

 private PatientResponse fetchPatient(String patientId) {
        String url = "http://localhost:8081/api/v1/patient/" + patientId;
        ResponseEntity<PatientResponse> response = restTemplate.getForEntity(url, PatientResponse.class);
        return response.getBody();
    }


}

  