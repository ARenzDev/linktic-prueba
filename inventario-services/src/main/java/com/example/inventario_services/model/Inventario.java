package com.example.inventario_services.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId;
    private Integer cantidad;
}
