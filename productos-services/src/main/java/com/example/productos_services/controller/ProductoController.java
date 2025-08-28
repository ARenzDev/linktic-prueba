package com.example.productos_services.controller;

import com.example.productos_services.model.JsonApiResponse;
import com.example.productos_services.model.Producto;
import com.example.productos_services.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // Crear producto
    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Map<String, Object> payload) {
        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        Map<String, Object> attributes = (Map<String, Object>) data.get("attributes");

        Producto producto = new Producto();
        producto.setNombre((String) attributes.get("nombre"));
        producto.setPrecio(Double.valueOf(attributes.get("precio").toString()));

        Producto saved = productoRepository.save(producto);

        JsonApiResponse<Producto> response = new JsonApiResponse<>(
            saved, "producto", String.valueOf(saved.getId())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getProducto(@PathVariable Long id) {
        Optional<Producto> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            JsonApiResponse<Producto> response = new JsonApiResponse<>(
                producto.get(), "producto", String.valueOf(producto.get().getId())
            );
            return ResponseEntity.ok(response);
        } else {
            // Error JSON API
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                Map.of("errors", List.of(Map.of(
                    "status", "404",
                    "title", "Producto no encontrado",
                    "detail", "No existe un producto con el ID proporcionado"
                )))
            );
        }
    }

    // Actualizar producto por ID
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isEmpty()) {
            return ResponseEntity.status(404)
                    .body(Map.of("errors", List.of(Map.of("detail", "Producto no encontrado"))));
        }
        Producto producto = productoOpt.get();
        Object dataObj = body.get("data");
        if (!(dataObj instanceof Map)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("errors", List.of(Map.of("detail", "Formato de datos inválido"))));
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) dataObj;

        Object attributesObj = data.get("attributes");
        if (!(attributesObj instanceof Map)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("errors", List.of(Map.of("detail", "Formato de atributos inválido"))));
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> attributes = (Map<String, Object>) attributesObj;

        if (attributes.containsKey("nombre"))
            producto.setNombre((String) attributes.get("nombre"));
        if (attributes.containsKey("precio"))
            producto.setPrecio(Double.valueOf(attributes.get("precio").toString()));

        Producto updated = productoRepository.save(producto);

        return ResponseEntity.ok(Map.of(
                "data", Map.of(
                        "type", "productos",
                        "id", updated.getId(),
                        "attributes", Map.of(
                                "nombre", updated.getNombre(),
                                "precio", updated.getPrecio()))));
    }

    // Eliminar producto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.status(404)
                    .body(Map.of("errors", List.of(Map.of("detail", "Producto no encontrado"))));
        }
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Listar productos con paginación
    @GetMapping
    public ResponseEntity<?> listarProductos(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var productos = productoRepository.findAll(PageRequest.of(page, size));
        List<Map<String, Object>> data = new ArrayList<>();
        for (Producto p : productos) {
            data.add(Map.of(
                    "type", "productos",
                    "id", p.getId(),
                    "attributes", Map.of(
                            "nombre", p.getNombre(),
                            "precio", p.getPrecio())));
        }
        return ResponseEntity.ok(Map.of("data", data));
    }
}
