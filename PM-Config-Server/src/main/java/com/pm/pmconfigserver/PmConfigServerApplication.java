package com.pm.pmconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class PmConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PmConfigServerApplication.class, args);
    }

}
