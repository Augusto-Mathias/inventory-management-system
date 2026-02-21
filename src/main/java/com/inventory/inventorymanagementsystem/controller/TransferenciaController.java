package com.inventory.inventorymanagementsystem.controller;

import com.inventory.inventorymanagementsystem.dto.TransferenciaCreateDTO;
import com.inventory.inventorymanagementsystem.dto.TransferenciaDTO;
import com.inventory.inventorymanagementsystem.model.enums.StatusTransferencia;
import com.inventory.inventorymanagementsystem.service.TransferenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciar transferências entre locais de estoque.
 *
 * Permite transferir múltiplos produtos de uma vez entre locais diferentes.
 * Cada transferência gera automaticamente as movimentações de estoque.
 *
 * Endpoints disponíveis:
 * - POST /api/transferencias - Criar transferência em lote
 * - GET /api/transferencias - Listar todas
 * - GET /api/transferencias/{id} - Buscar por ID
 * - GET /api/transferencias/status/{status} - Por status
 * - GET /api/transferencias/local-origem/{id} - Saídas de um local
 * - GET /api/transferencias/local-destino/{id} - Chegadas em um local
 * - GET /api/transferencias/produto/{id} - Por produto
 */
@RestController
@RequestMapping("/api/transferencias")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    /**
     * Cria uma nova transferência entre locais.
     *
     * Exemplo de body:
     * {
     *   "localOrigemId": 1,
     *   "localDestinoId": 2,
     *   "itens": [
     *     { "produtoId": 1, "quantidade": 105 },
     *     { "produtoId": 2, "quantidade": 5 },
     *     { "produtoId": 3, "quantidade": 10 }
     *   ],
     *   "observacao": "Reposição Full ML SC",
     *   "usuarioId": 6
     * }
     */
    @PostMapping
    public ResponseEntity<TransferenciaDTO> criar(@Valid @RequestBody TransferenciaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transferenciaService.criar(dto));
    }

    /**
     * Busca transferência por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transferenciaService.buscarPorId(id));
    }

    /**
     * Lista todas as transferências (ordenadas por data decrescente)
     */
    @GetMapping
    public ResponseEntity<List<TransferenciaDTO>> listarTodas() {
        return ResponseEntity.ok(transferenciaService.listarTodas());
    }

    /**
     * Lista transferências por status
     *
     * Exemplos:
     * - GET /api/transferencias/status/CONCLUIDA
     * - GET /api/transferencias/status/PENDENTE
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TransferenciaDTO>> listarPorStatus(@PathVariable StatusTransferencia status) {
        return ResponseEntity.ok(transferenciaService.listarPorStatus(status));
    }

    /**
     * Lista transferências que SAÍRAM de um local específico
     */
    @GetMapping("/local-origem/{localOrigemId}")
    public ResponseEntity<List<TransferenciaDTO>> listarPorLocalOrigem(@PathVariable Long localOrigemId) {
        return ResponseEntity.ok(transferenciaService.listarPorLocalOrigem(localOrigemId));
    }

    /**
     * Lista transferências que CHEGARAM em um local específico
     */
    @GetMapping("/local-destino/{localDestinoId}")
    public ResponseEntity<List<TransferenciaDTO>> listarPorLocalDestino(@PathVariable Long localDestinoId) {
        return ResponseEntity.ok(transferenciaService.listarPorLocalDestino(localDestinoId));
    }

    /**
     * Lista transferências que movimentaram um produto específico
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<TransferenciaDTO>> listarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(transferenciaService.listarPorProduto(produtoId));
    }
}