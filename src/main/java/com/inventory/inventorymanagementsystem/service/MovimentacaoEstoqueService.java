package com.inventory.inventorymanagementsystem.service;

import com.inventory.inventorymanagementsystem.dto.MovimentacaoEstoqueCreateDTO;
import com.inventory.inventorymanagementsystem.dto.MovimentacaoEstoqueDTO;
import com.inventory.inventorymanagementsystem.exception.BadRequestException;
import com.inventory.inventorymanagementsystem.exception.ResourceNotFoundException;
import com.inventory.inventorymanagementsystem.model.*;
import com.inventory.inventorymanagementsystem.model.enums.MotivoMovimentacao;
import com.inventory.inventorymanagementsystem.model.enums.TipoMovimentacao;
import com.inventory.inventorymanagementsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsável pela lógica de negócio de movimentações de estoque.
 *
 * Regras principais:
 * - Toda movimentação DEVE atualizar o EstoqueProduto correspondente
 * - Saídas não podem deixar estoque negativo
 * - Transferências criam duas movimentações (saída + entrada)
 * - Registra quantidade anterior e atual para auditoria
 */
@Service
@RequiredArgsConstructor
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;
    private final EstoqueProdutoRepository estoqueProdutoRepository;
    private final ProdutoRepository produtoRepository;
    private final LocalEstoqueRepository localEstoqueRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Registra uma nova movimentação de estoque.
     *
     * Fluxo:
     * 1. Valida dados (produto, local, usuario)
     * 2. Busca ou cria registro de estoque
     * 3. Valida se há estoque suficiente (em caso de saída)
     * 4. Atualiza quantidade no estoque
     * 5. Registra a movimentação com quantidade anterior e atual
     *
     * @param dto dados da movimentação
     * @return movimentação criada
     */
    @Transactional
    public MovimentacaoEstoqueDTO criar(MovimentacaoEstoqueCreateDTO dto) {
        // Busca e valida produto
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto", dto.getProdutoId()));

        // Busca e valida local
        LocalEstoque localEstoque = localEstoqueRepository.findById(dto.getLocalEstoqueId())
                .orElseThrow(() -> new ResourceNotFoundException("Local de Estoque", dto.getLocalEstoqueId()));

        // Busca e valida usuário
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getUsuarioId()));

        // Busca ou cria registro de estoque
        EstoqueProduto estoque = estoqueProdutoRepository
                .findByProdutoAndLocalEstoque(produto, localEstoque)
                .orElseGet(() -> {
                    // Se não existe, cria com quantidade zero
                    EstoqueProduto novoEstoque = new EstoqueProduto();
                    novoEstoque.setProduto(produto);
                    novoEstoque.setLocalEstoque(localEstoque);
                    novoEstoque.setQuantidade(0);
                    return estoqueProdutoRepository.save(novoEstoque);
                });

        // Guarda quantidade anterior para auditoria
        Integer quantidadeAnterior = estoque.getQuantidade();

        // Calcula nova quantidade baseado no tipo de movimentação
        Integer novaQuantidade = calcularNovaQuantidade(
                quantidadeAnterior,
                dto.getQuantidade(),
                dto.getTipo()
        );

        // Valida se não vai ficar com estoque negativo
        if (novaQuantidade < 0) {
            throw new BadRequestException(
                    "Estoque insuficiente. Disponivel: " + quantidadeAnterior +
                            ", Solicitado: " + Math.abs(dto.getQuantidade())
            );
        }

        // Atualiza quantidade no estoque
        estoque.setQuantidade(novaQuantidade);
        estoqueProdutoRepository.save(estoque);

        // Cria o registro de movimentação
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setLocalEstoque(localEstoque);
        movimentacao.setTipo(dto.getTipo());
        movimentacao.setMotivo(dto.getMotivo());  // ← MOVER PARA AQUI
        movimentacao.setQuantidade(dto.getQuantidade());
        movimentacao.setQuantidadeAnterior(quantidadeAnterior);
        movimentacao.setQuantidadeAtual(novaQuantidade);
        movimentacao.setObservacao(dto.getObservacao());
        movimentacao.setUsuario(usuario);

        // Se for transferência, valida e registra local de destino
        if (dto.getTipo() == TipoMovimentacao.TRANSFERENCIA) {
            if (dto.getLocalDestinoId() == null) {
                throw new BadRequestException("Local de destino e obrigatorio para transferencias");
            }
            LocalEstoque localDestino = localEstoqueRepository.findById(dto.getLocalDestinoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Local de Destino", dto.getLocalDestinoId()));
            movimentacao.setLocalDestino(localDestino);
        }

        return toDTO(movimentacaoEstoqueRepository.save(movimentacao));
    }

    /**
     * Calcula a nova quantidade baseado no tipo de movimentação.
     *
     * ENTRADA: soma a quantidade
     * SAIDA: subtrai a quantidade
     * TRANSFERENCIA: subtrai (saída do local origem)
     * AJUSTE_INVENTARIO: define a quantidade exata
     */
    private Integer calcularNovaQuantidade(Integer quantidadeAtual, Integer quantidade, TipoMovimentacao tipo) {
        switch (tipo) {
            case ENTRADA:
                return quantidadeAtual + quantidade;
            case SAIDA:
            case TRANSFERENCIA:
                return quantidadeAtual - Math.abs(quantidade); // Garante que quantidade seja positiva
            case AJUSTE_INVENTARIO:
                return quantidade; // Define quantidade exata
            default:
                return quantidadeAtual;
        }
    }

    /**
     * Busca movimentação por ID
     */
    @Transactional(readOnly = true)
    public MovimentacaoEstoqueDTO buscarPorId(Long id) {
        return toDTO(movimentacaoEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimentacao", id)));
    }

    /**
     * Lista todas as movimentações (ordenadas por data decrescente)
     */
    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueDTO> listarTodas() {
        return movimentacaoEstoqueRepository.findUltimasMovimentacoes().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista movimentações de um produto específico
     */
    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueDTO> listarPorProduto(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", produtoId));
        return movimentacaoEstoqueRepository.findByProduto(produto).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista movimentações de um local específico
     */
    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueDTO> listarPorLocal(Long localEstoqueId) {
        LocalEstoque localEstoque = localEstoqueRepository.findById(localEstoqueId)
                .orElseThrow(() -> new ResourceNotFoundException("Local de Estoque", localEstoqueId));
        return movimentacaoEstoqueRepository.findByLocalEstoque(localEstoque).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista movimentações por tipo (ENTRADA, SAIDA, etc)
     */
    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueDTO> listarPorTipo(TipoMovimentacao tipo) {
        return movimentacaoEstoqueRepository.findByTipo(tipo).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista movimentações por motivo específico
     */
    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueDTO> listarPorMotivo(MotivoMovimentacao motivo) {
        return movimentacaoEstoqueRepository.findByMotivo(motivo).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista movimentações dentro de um período
     */
    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueDTO> listarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return movimentacaoEstoqueRepository.findByPeriodo(dataInicio, dataFim).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converte Entity para DTO
     */
    private MovimentacaoEstoqueDTO toDTO(MovimentacaoEstoque movimentacao) {
        return MovimentacaoEstoqueDTO.builder()
                .id(movimentacao.getId())
                .produtoId(movimentacao.getProduto().getId())
                .produtoNome(movimentacao.getProduto().getNome())
                .produtoSku(movimentacao.getProduto().getSku())
                .localEstoqueId(movimentacao.getLocalEstoque().getId())
                .localEstoqueNome(movimentacao.getLocalEstoque().getNome())
                .tipo(movimentacao.getTipo())
                .motivo(movimentacao.getMotivo())
                .quantidade(movimentacao.getQuantidade())
                .quantidadeAnterior(movimentacao.getQuantidadeAnterior())
                .quantidadeAtual(movimentacao.getQuantidadeAtual())
                .observacao(movimentacao.getObservacao())
                .usuarioId(movimentacao.getUsuario().getId())
                .usuarioNome(movimentacao.getUsuario().getNome())
                .localDestinoId(movimentacao.getLocalDestino() != null ? movimentacao.getLocalDestino().getId() : null)
                .localDestinoNome(movimentacao.getLocalDestino() != null ? movimentacao.getLocalDestino().getNome() : null)
                .createdAt(movimentacao.getCreatedAt())
                .build();
    }
}