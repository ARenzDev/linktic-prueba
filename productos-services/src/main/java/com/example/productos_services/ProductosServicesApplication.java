package com.example.productos_services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProductosServicesApplication {
    private static final Logger logger = LoggerFactory.getLogger(ProductosServicesApplication.class);

    public static void main(String[] args) {
        logger.info("Iniciando ProductosServicesApplication...");
        SpringApplication.run(ProductosServicesApplication.class, args);
        logger.info("ProductosServicesApplication iniciado correctamente.");
    }
}
