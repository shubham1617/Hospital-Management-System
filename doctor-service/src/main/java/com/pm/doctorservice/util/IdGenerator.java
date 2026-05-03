package com.pm.doctorservice.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGenerator {

    /**
     * Generate unique doctor ID with prefix
     */
    public String generateDoctorId() {
        return "DOC_" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
}
