package com.pm.pmeurekasever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class PmEurekaSeverApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmEurekaSeverApplication.class, args);
    }

}
