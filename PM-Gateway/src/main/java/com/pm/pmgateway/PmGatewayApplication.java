package com.pm.pmgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PmGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmGatewayApplication.class, args);
    }

}
