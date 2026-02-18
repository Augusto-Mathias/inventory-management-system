package com.inventory.inventorymanagementsystem.controller;

import com.inventory.inventorymanagementsystem.dto.MovimentacaoEstoqueCreateDTO;
import com.inventory.inventorymanagementsystem.dto.MovimentacaoEstoqueDTO;
import com.inventory.inventorymanagementsystem.model.enums.MotivoMovimentacao;
import com.inventory.inventorymanagementsystem.model.enums.TipoMovimentacao;
import com.inventory.inventorymanagementsystem.service.MovimentacaoEstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller REST para gerenciar movimentações de estoque.
 *
 * Endpoints disponíveis:
 * - POST /api/movimentacoes - Registrar nova movimentação
 * - GET /api/movimentacoes - Listar todas
 * - GET /api/movimentacoes/{id} - Buscar por ID
 * - GET /api/movimentacoes/produto/{produtoId} - Por produto
 * - GET /api/movimentacoes/local/{localId} - Por local
 * - GET /api/movimentacoes/tipo/{tipo} - Por tipo
 * - GET /api/movimentacoes/periodo - Por período de datas
 */
@RestController
@RequestMapping("/api/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoEstoqueService;

    /**
     * Registra uma nova movimentação de estoque.
     *
     * Exemplo de body:
     * {
     *   "produtoId": 1,
     *   "localEstoqueId": 1,
     *   "tipo": "ENTRADA",
     *   "quantidade": 20,
     *   "observacao": "Compra NF 12345",
     *   "usuarioId": 1
     * }
     */
    @PostMapping
    public ResponseEntity<MovimentacaoEstoqueDTO> criar(@Valid @RequestBody MovimentacaoEstoqueCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoEstoqueService.criar(dto));
    }

    /**
     * Lista todas as movimentações (ordenadas por data decrescente)
     */
    @GetMapping
    public ResponseEntity<List<MovimentacaoEstoqueDTO>> listarTodas() {
        return ResponseEntity.ok(movimentacaoEstoqueService.listarTodas());
    }

    /**
     * Busca movimentação por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MovimentacaoEstoqueDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(movimentacaoEstoqueService.buscarPorId(id));
    }

    /**
     * Lista todas as movimentações de um produto específico
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<MovimentacaoEstoqueDTO>> listarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(movimentacaoEstoqueService.listarPorProduto(produtoId));
    }

    /**
     * Lista todas as movimentações de um local específico
     */
    @GetMapping("/local/{localEstoqueId}")
    public ResponseEntity<List<MovimentacaoEstoqueDTO>> listarPorLocal(@PathVariable Long localEstoqueId) {
        return ResponseEntity.ok(movimentacaoEstoqueService.listarPorLocal(localEstoqueId));
    }

    /**
     * Lista movimentações por tipo (ENTRADA, SAIDA, TRANSFERENCIA, AJUSTE_INVENTARIO)
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<MovimentacaoEstoqueDTO>> listarPorTipo(@PathVariable TipoMovimentacao tipo) {
        return ResponseEntity.ok(movimentacaoEstoqueService.listarPorTipo(tipo));
    }

    /**
     * Lista movimentações por motivo específico
     *
     * Exemplos:
     * - GET /api/movimentacoes/motivo/VENDA
     * - GET /api/movimentacoes/motivo/COMPRA_FORNECEDOR
     * - GET /api/movimentacoes/motivo/DEVOLUCAO_CLIENTE
     */
    @GetMapping("/motivo/{motivo}")
    public ResponseEntity<List<MovimentacaoEstoqueDTO>> listarPorMotivo(@PathVariable MotivoMovimentacao motivo) {
        return ResponseEntity.ok(movimentacaoEstoqueService.listarPorMotivo(motivo));
    }

    /**
     * Lista movimentações dentro de um período de datas.
     *
     * Exemplo: /api/movimentacoes/periodo?dataInicio=2026-02-01T00:00:00&dataFim=2026-02-28T23:59:59
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<MovimentacaoEstoqueDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {
        return ResponseEntity.ok(movimentacaoEstoqueService.listarPorPeriodo(dataInicio, dataFim));
    }
}