package com.tiotec.microservicio.consejo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/consejos")
public class ConsejoController {

    private final ConsejoRepository repository;

    public ConsejoController(ConsejoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Consejo> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Consejo obtener(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Consejo no encontrado"));
    }

    @PostMapping
    public ResponseEntity<Consejo> crear(@Valid @RequestBody Consejo consejo) {
        consejo.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(consejo));
    }

    @PutMapping("/{id}")
    public Consejo actualizar(@PathVariable Long id, @Valid @RequestBody Consejo consejo) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Consejo no encontrado");
        }
        consejo.setId(id);
        return repository.save(consejo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Consejo no encontrado");
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
