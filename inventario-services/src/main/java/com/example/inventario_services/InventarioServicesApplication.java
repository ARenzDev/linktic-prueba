package com.example.inventario_services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InventarioServicesApplication {
    private static final Logger logger = LoggerFactory.getLogger(InventarioServicesApplication.class);

    public static void main(String[] args) {
        logger.info("Iniciando InventarioServicesApplication...");
        SpringApplication.run(InventarioServicesApplication.class, args);
        logger.info("InventarioServicesApplication iniciado correctamente.");
    }

}
