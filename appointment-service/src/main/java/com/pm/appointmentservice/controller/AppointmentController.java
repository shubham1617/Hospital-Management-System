package com.pm.appointmentservice.controller;

import com.pm.appointmentservice.dto.AppointmentDTO;
import com.pm.appointmentservice.dto.validation.CreateAppValidator;
import com.pm.appointmentservice.dto.validation.UpdateAppValidator;
import com.pm.appointmentservice.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@Validated(CreateAppValidator.class) @RequestBody AppointmentDTO appointmentDTO) {
        return appointmentService.createAppointment(appointmentDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(@Validated(UpdateAppValidator.class) @PathVariable String id, @RequestBody AppointmentDTO appointmentDTO){
        return appointmentService.rescheduleAppointment(id,appointmentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable String id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Deleted Successfully");
    }
}