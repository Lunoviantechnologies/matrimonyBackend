package com.example.matrimony;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MatrimonyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatrimonyApplication.class, args);
    }
}
