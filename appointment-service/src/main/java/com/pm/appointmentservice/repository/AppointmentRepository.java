package com.pm.appointmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pm.appointmentservice.model.Appointments;

public interface AppointmentRepository extends JpaRepository<Appointments,String>
{

    
} 