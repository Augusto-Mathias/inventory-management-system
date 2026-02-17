package com.inventory.inventorymanagementsystem.controller;

import com.inventory.inventorymanagementsystem.dto.LocalEstoqueCreateDTO;
import com.inventory.inventorymanagementsystem.dto.LocalEstoqueDTO;
import com.inventory.inventorymanagementsystem.model.enums.TipoLocal;
import com.inventory.inventorymanagementsystem.service.LocalEstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locais-estoque")
@RequiredArgsConstructor
public class LocalEstoqueController {

    private final LocalEstoqueService localEstoqueService;

    @PostMapping
    public ResponseEntity<LocalEstoqueDTO> criar(@Valid @RequestBody LocalEstoqueCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(localEstoqueService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<LocalEstoqueDTO>> listarTodos() {
        return ResponseEntity.ok(localEstoqueService.listarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<LocalEstoqueDTO>> listarAtivos() {
        return ResponseEntity.ok(localEstoqueService.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocalEstoqueDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(localEstoqueService.buscarPorId(id));
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<LocalEstoqueDTO>> listarPorTipo(@PathVariable TipoLocal tipo) {
        return ResponseEntity.ok(localEstoqueService.listarPorTipo(tipo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocalEstoqueDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LocalEstoqueCreateDTO dto) {
        return ResponseEntity.ok(localEstoqueService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        localEstoqueService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}