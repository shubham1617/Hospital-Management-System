package com.pm.doctorservice;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Disabled("Skipping tests temporarily")
@SpringBootTest
@Import(EurekaTestConfiguration.class)
class DoctorServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
