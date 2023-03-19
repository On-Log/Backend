package com.nanal.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class NanalApplication {

    public static void main(String[] args) {
        SpringApplication.run(NanalApplication.class, args);
    }

}
