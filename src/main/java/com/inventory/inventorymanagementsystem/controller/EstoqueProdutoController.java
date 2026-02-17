package com.inventory.inventorymanagementsystem.controller;

import com.inventory.inventorymanagementsystem.dto.EstoqueProdutoCreateDTO;
import com.inventory.inventorymanagementsystem.dto.EstoqueProdutoDTO;
import com.inventory.inventorymanagementsystem.dto.EstoqueProdutoUpdateDTO;
import com.inventory.inventorymanagementsystem.service.EstoqueProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
@RequiredArgsConstructor
public class EstoqueProdutoController {

    private final EstoqueProdutoService estoqueProdutoService;

    @PostMapping
    public ResponseEntity<EstoqueProdutoDTO> criar(@Valid @RequestBody EstoqueProdutoCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estoqueProdutoService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<EstoqueProdutoDTO>> listarTodos() {
        return ResponseEntity.ok(estoqueProdutoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstoqueProdutoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estoqueProdutoService.buscarPorId(id));
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<EstoqueProdutoDTO>> listarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(estoqueProdutoService.listarPorProduto(produtoId));
    }

    @GetMapping("/local/{localEstoqueId}")
    public ResponseEntity<List<EstoqueProdutoDTO>> listarPorLocal(@PathVariable Long localEstoqueId) {
        return ResponseEntity.ok(estoqueProdutoService.listarPorLocal(localEstoqueId));
    }

    @GetMapping("/produto/{produtoId}/quantidade-total")
    public ResponseEntity<Integer> obterQuantidadeTotal(@PathVariable Long produtoId) {
        return ResponseEntity.ok(estoqueProdutoService.obterQuantidadeTotal(produtoId));
    }

    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<EstoqueProdutoDTO>> listarEstoqueBaixo() {
        return ResponseEntity.ok(estoqueProdutoService.listarEstoqueBaixo());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstoqueProdutoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EstoqueProdutoUpdateDTO dto) {
        return ResponseEntity.ok(estoqueProdutoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        estoqueProdutoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}