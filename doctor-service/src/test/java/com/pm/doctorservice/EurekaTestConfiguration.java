package com.pm.doctorservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Test configuration to disable Spring Cloud auto-configuration
 */
@TestConfiguration
public class EurekaTestConfiguration {
    
    // Empty configuration to prevent Eureka initialization issues in tests
}

