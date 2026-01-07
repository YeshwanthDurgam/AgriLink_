package com.agrilink.farm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Farm Service.
 * Handles farm, field, and crop plan management.
 */
@SpringBootApplication
public class FarmServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmServiceApplication.class, args);
    }
}
