package com.inventory.inventorymanagementsystem.controller;

import com.inventory.inventorymanagementsystem.dto.FornecedorCreateDTO;
import com.inventory.inventorymanagementsystem.dto.FornecedorDTO;
import com.inventory.inventorymanagementsystem.service.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @PostMapping
    public ResponseEntity<FornecedorDTO> criar(@Valid @RequestBody FornecedorCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<FornecedorDTO>> listarTodos() {
        return ResponseEntity.ok(fornecedorService.listarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<FornecedorDTO>> listarAtivos() {
        return ResponseEntity.ok(fornecedorService.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(fornecedorService.buscarPorId(id));
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<FornecedorDTO> buscarPorCnpj(@PathVariable String cnpj) {
        return ResponseEntity.ok(fornecedorService.buscarPorCnpj(cnpj));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<FornecedorDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(fornecedorService.buscarPorNome(nome));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FornecedorCreateDTO dto) {
        return ResponseEntity.ok(fornecedorService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fornecedorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}