package com.pm.doctorservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
      
        return new OpenAPI()
                // .servers(List.of(
                //         new Server().url("http://localhost:8081").description("Development server"),
                //         new Server().url("http://localhost:8080").description("Production server")
                // ))
                .info(new Info()
                        .title("Doctor Service API")
                        .version("1.0.0")
                        .description("RESTful API for managing doctors in the Patient Management System")
                        .contact(new Contact()
                                .name("Doctor Service Team")
                                .email("support@patientmanagement.com")
                        )
                );
    }
}
