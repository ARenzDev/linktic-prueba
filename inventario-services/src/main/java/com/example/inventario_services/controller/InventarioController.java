package com.example.inventario_services.controller;

import com.example.inventario_services.model.Inventario;
import com.example.inventario_services.model.JsonApiResponse;
import com.example.inventario_services.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/inventarios")
public class InventarioController {

        @Autowired
        private InventarioRepository inventarioRepository;

        // Consultar cantidad disponible de un producto
        @GetMapping("/{productoId}")
        public ResponseEntity<?> consultarInventario(@PathVariable Long productoId) {
                Inventario inventario = inventarioRepository.findByProductoId(productoId);
                if (inventario == null) {
                        return ResponseEntity.status(404)
                                        .body(Map.of("errors", List.of(Map.of("detail", "Inventario no encontrado"))));
                }
                return ResponseEntity.ok(Map.of(
                                "data", Map.of(
                                                "type", "inventarios",
                                                "id", inventario.getId(),
                                                "attributes", Map.of(
                                                                "productoId", inventario.getProductoId(),
                                                                "cantidad", inventario.getCantidad()))));
        }

        // Obtener inventario por ID
        @GetMapping("/{id}")
        public ResponseEntity<?> getInventario(@PathVariable Long id) {
                Optional<Inventario> inventario = inventarioRepository.findById(id);
                if (inventario.isPresent()) {
                        JsonApiResponse<Inventario> response = new JsonApiResponse<>(
                                        inventario.get(), "inventario", String.valueOf(inventario.get().getId()));
                        return ResponseEntity.ok(response);
                } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                        Map.of("errors", List.of(Map.of(
                                                        "status", "404",
                                                        "title", "Inventario no encontrado",
                                                        "detail", "No existe inventario con el ID proporcionado"))));
                }
        }

        // Actualizar cantidad tras compra
        @PatchMapping("/{productoId}")
        public ResponseEntity<?> actualizarInventario(@PathVariable Long productoId,
                        @RequestBody Map<String, Object> body) {
                Inventario inventario = inventarioRepository.findByProductoId(productoId);
                if (inventario == null) {
                        return ResponseEntity.status(404)
                                        .body(Map.of("errors", List.of(Map.of("detail", "Inventario no encontrado"))));
                }
                Map<String, Object> data = (Map<String, Object>) body.get("data");
                Map<String, Object> attributes = (Map<String, Object>) data.get("attributes");
                if (attributes.containsKey("cantidad")) {
                        inventario.setCantidad((Integer) attributes.get("cantidad"));
                }
                Inventario updated = inventarioRepository.save(inventario);

                // Evento simple: mensaje en consola
                System.out.println("Inventario actualizado para producto " + productoId + ": " + updated.getCantidad());

                return ResponseEntity.ok(Map.of(
                                "data", Map.of(
                                                "type", "inventarios",
                                                "id", updated.getId(),
                                                "attributes", Map.of(
                                                                "productoId", updated.getProductoId(),
                                                                "cantidad", updated.getCantidad()))));
        }
}
