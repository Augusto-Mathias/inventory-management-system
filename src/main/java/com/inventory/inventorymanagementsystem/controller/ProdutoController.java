package com.inventory.inventorymanagementsystem.controller;

import com.inventory.inventorymanagementsystem.dto.ProdutoCreateDTO;
import com.inventory.inventorymanagementsystem.dto.ProdutoDTO;
import com.inventory.inventorymanagementsystem.model.enums.StatusProduto;
import com.inventory.inventorymanagementsystem.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoDTO> criar(@Valid @RequestBody ProdutoCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ProdutoDTO>> listarAtivos() {
        return ResponseEntity.ok(produtoService.listarAtivos());
    }

    @GetMapping("/destaques")
    public ResponseEntity<List<ProdutoDTO>> listarDestaques() {
        return ResponseEntity.ok(produtoService.listarDestaques());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProdutoDTO> buscarPorSku(@PathVariable String sku) {
        return ResponseEntity.ok(produtoService.buscarPorSku(sku));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoDTO>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(produtoService.buscarPorNome(nome));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProdutoDTO>> listarPorStatus(@PathVariable StatusProduto status) {
        return ResponseEntity.ok(produtoService.listarPorStatus(status));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoDTO>> listarPorCategoria(@PathVariable Long categoriaId) {
        return ResponseEntity.ok(produtoService.listarPorCategoria(categoriaId));
    }

    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<ProdutoDTO>> listarPorFornecedor(@PathVariable Long fornecedorId) {
        return ResponseEntity.ok(produtoService.listarPorFornecedor(fornecedorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoCreateDTO dto) {
        return ResponseEntity.ok(produtoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}