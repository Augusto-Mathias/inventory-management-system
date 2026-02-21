package com.inventory.inventorymanagementsystem.service;

import com.inventory.inventorymanagementsystem.dto.ItemTransferenciaDTO;
import com.inventory.inventorymanagementsystem.dto.TransferenciaCreateDTO;
import com.inventory.inventorymanagementsystem.dto.TransferenciaDTO;
import com.inventory.inventorymanagementsystem.exception.BadRequestException;
import com.inventory.inventorymanagementsystem.exception.ResourceNotFoundException;
import com.inventory.inventorymanagementsystem.model.*;
import com.inventory.inventorymanagementsystem.model.enums.MotivoMovimentacao;
import com.inventory.inventorymanagementsystem.model.enums.StatusTransferencia;
import com.inventory.inventorymanagementsystem.model.enums.TipoMovimentacao;
import com.inventory.inventorymanagementsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsável pela lógica de negócio de transferências entre locais.
 *
 * Fluxo de uma transferência:
 * 1. Valida locais de origem e destino (devem ser diferentes)
 * 2. Valida estoque disponível para cada produto
 * 3. Cria a transferência com status PENDENTE
 * 4. Para cada item, cria 2 movimentações automáticas:
 *    - SAIDA do local origem
 *    - ENTRADA no local destino
 * 5. Atualiza status para CONCLUIDA
 */
@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private final TransferenciaRepository transferenciaRepository;
    private final ItemTransferenciaRepository itemTransferenciaRepository;
    private final LocalEstoqueRepository localEstoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstoqueProdutoRepository estoqueProdutoRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    /**
     * Cria uma nova transferência entre locais.
     *
     * Processo:
     * 1. Valida dados (locais, produtos, estoque)
     * 2. Cria transferência
     * 3. Cria itens da transferência
     * 4. Gera movimentações de estoque automaticamente
     * 5. Atualiza estoques
     */
    @Transactional
    public TransferenciaDTO criar(TransferenciaCreateDTO dto) {
        // Busca e valida local de origem
        LocalEstoque localOrigem = localEstoqueRepository.findById(dto.getLocalOrigemId())
                .orElseThrow(() -> new ResourceNotFoundException("Local de Origem", dto.getLocalOrigemId()));

        // Busca e valida local de destino
        LocalEstoque localDestino = localEstoqueRepository.findById(dto.getLocalDestinoId())
                .orElseThrow(() -> new ResourceNotFoundException("Local de Destino", dto.getLocalDestinoId()));

        // Valida que origem e destino são diferentes
        if (localOrigem.getId().equals(localDestino.getId())) {
            throw new BadRequestException("Local de origem e destino devem ser diferentes");
        }

        // Busca e valida usuário
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", dto.getUsuarioId()));

        // Cria a transferência
        Transferencia transferencia = new Transferencia();
        transferencia.setLocalOrigem(localOrigem);
        transferencia.setLocalDestino(localDestino);
        transferencia.setStatus(StatusTransferencia.PENDENTE);
        transferencia.setObservacao(dto.getObservacao());
        transferencia.setUsuario(usuario);

        // Salva transferência
        transferencia = transferenciaRepository.save(transferencia);

        // Processa cada item da transferência
        for (var itemDto : dto.getItens()) {
            // Busca produto
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto", itemDto.getProdutoId()));

            // Busca estoque no local de origem
            EstoqueProduto estoqueOrigem = estoqueProdutoRepository
                    .findByProdutoAndLocalEstoque(produto, localOrigem)
                    .orElseThrow(() -> new BadRequestException(
                            "Produto " + produto.getNome() + " nao possui estoque em " + localOrigem.getNome()
                    ));

            // Valida se há estoque suficiente
            if (estoqueOrigem.getQuantidade() < itemDto.getQuantidade()) {
                throw new BadRequestException(
                        "Estoque insuficiente de " + produto.getNome() + " em " + localOrigem.getNome() +
                                ". Disponivel: " + estoqueOrigem.getQuantidade() +
                                ", Solicitado: " + itemDto.getQuantidade()
                );
            }

            // Cria item da transferência
            ItemTransferencia item = new ItemTransferencia();
            item.setTransferencia(transferencia);
            item.setProduto(produto);
            item.setQuantidade(itemDto.getQuantidade());
            itemTransferenciaRepository.save(item);

            // ========== CRIA MOVIMENTAÇÕES AUTOMÁTICAS ==========

            // 1. SAÍDA do local origem
            Integer quantidadeAnteriorOrigem = estoqueOrigem.getQuantidade();
            estoqueOrigem.setQuantidade(quantidadeAnteriorOrigem - itemDto.getQuantidade());
            estoqueProdutoRepository.save(estoqueOrigem);

            MovimentacaoEstoque movimentacaoSaida = new MovimentacaoEstoque();
            movimentacaoSaida.setProduto(produto);
            movimentacaoSaida.setLocalEstoque(localOrigem);
            movimentacaoSaida.setTipo(TipoMovimentacao.TRANSFERENCIA);
            movimentacaoSaida.setMotivo(MotivoMovimentacao.ENVIO_TRANSFERENCIA);
            movimentacaoSaida.setQuantidade(itemDto.getQuantidade());
            movimentacaoSaida.setQuantidadeAnterior(quantidadeAnteriorOrigem);
            movimentacaoSaida.setQuantidadeAtual(estoqueOrigem.getQuantidade());
            movimentacaoSaida.setObservacao("Transferencia #" + transferencia.getId() + " para " + localDestino.getNome());
            movimentacaoSaida.setUsuario(usuario);
            movimentacaoSaida.setLocalDestino(localDestino);
            movimentacaoEstoqueRepository.save(movimentacaoSaida);

            // 2. ENTRADA no local destino
            EstoqueProduto estoqueDestino = estoqueProdutoRepository
                    .findByProdutoAndLocalEstoque(produto, localDestino)
                    .orElseGet(() -> {
                        // Se não existe, cria com quantidade zero
                        EstoqueProduto novoEstoque = new EstoqueProduto();
                        novoEstoque.setProduto(produto);
                        novoEstoque.setLocalEstoque(localDestino);
                        novoEstoque.setQuantidade(0);
                        return estoqueProdutoRepository.save(novoEstoque);
                    });

            Integer quantidadeAnteriorDestino = estoqueDestino.getQuantidade();
            estoqueDestino.setQuantidade(quantidadeAnteriorDestino + itemDto.getQuantidade());
            estoqueProdutoRepository.save(estoqueDestino);

            MovimentacaoEstoque movimentacaoEntrada = new MovimentacaoEstoque();
            movimentacaoEntrada.setProduto(produto);
            movimentacaoEntrada.setLocalEstoque(localDestino);
            movimentacaoEntrada.setTipo(TipoMovimentacao.TRANSFERENCIA);
            movimentacaoEntrada.setMotivo(MotivoMovimentacao.RECEBIMENTO_TRANSFERENCIA);
            movimentacaoEntrada.setQuantidade(itemDto.getQuantidade());
            movimentacaoEntrada.setQuantidadeAnterior(quantidadeAnteriorDestino);
            movimentacaoEntrada.setQuantidadeAtual(estoqueDestino.getQuantidade());
            movimentacaoEntrada.setObservacao("Transferencia #" + transferencia.getId() + " de " + localOrigem.getNome());
            movimentacaoEntrada.setUsuario(usuario);
            movimentacaoEstoqueRepository.save(movimentacaoEntrada);
        }

        // Atualiza status para CONCLUIDA
        transferencia.setStatus(StatusTransferencia.CONCLUIDA);
        transferencia = transferenciaRepository.save(transferencia);

        return toDTO(transferencia);
    }

    /**
     * Busca transferência por ID
     */
    @Transactional(readOnly = true)
    public TransferenciaDTO buscarPorId(Long id) {
        return toDTO(transferenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transferencia", id)));
    }

    /**
     * Lista todas as transferências
     */
    @Transactional(readOnly = true)
    public List<TransferenciaDTO> listarTodas() {
        return transferenciaRepository.findUltimasTransferencias().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista transferências por status
     */
    @Transactional(readOnly = true)
    public List<TransferenciaDTO> listarPorStatus(StatusTransferencia status) {
        return transferenciaRepository.findByStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista transferências que SAÍRAM de um local específico
     */
    @Transactional(readOnly = true)
    public List<TransferenciaDTO> listarPorLocalOrigem(Long localOrigemId) {
        LocalEstoque localOrigem = localEstoqueRepository.findById(localOrigemId)
                .orElseThrow(() -> new ResourceNotFoundException("Local de Origem", localOrigemId));
        return transferenciaRepository.findByLocalOrigem(localOrigem).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista transferências que CHEGARAM em um local específico
     */
    @Transactional(readOnly = true)
    public List<TransferenciaDTO> listarPorLocalDestino(Long localDestinoId) {
        LocalEstoque localDestino = localEstoqueRepository.findById(localDestinoId)
                .orElseThrow(() -> new ResourceNotFoundException("Local de Destino", localDestinoId));
        return transferenciaRepository.findByLocalDestino(localDestino).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista transferências de um produto específico
     * (busca em todos os itens de todas as transferências)
     */
    @Transactional(readOnly = true)
    public List<TransferenciaDTO> listarPorProduto(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", produtoId));

        return transferenciaRepository.findAll().stream()
                .filter(t -> t.getItens().stream()
                        .anyMatch(item -> item.getProduto().getId().equals(produtoId)))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converte Entity para DTO
     */
    private TransferenciaDTO toDTO(Transferencia transferencia) {
        List<ItemTransferenciaDTO> itensDTO = transferencia.getItens().stream()
                .map(item -> ItemTransferenciaDTO.builder()
                        .id(item.getId())
                        .produtoId(item.getProduto().getId())
                        .produtoNome(item.getProduto().getNome())
                        .produtoSku(item.getProduto().getSku())
                        .quantidade(item.getQuantidade())
                        .build())
                .collect(Collectors.toList());

        return TransferenciaDTO.builder()
                .id(transferencia.getId())
                .localOrigemId(transferencia.getLocalOrigem().getId())
                .localOrigemNome(transferencia.getLocalOrigem().getNome())
                .localDestinoId(transferencia.getLocalDestino().getId())
                .localDestinoNome(transferencia.getLocalDestino().getNome())
                .status(transferencia.getStatus())
                .observacao(transferencia.getObservacao())
                .usuarioId(transferencia.getUsuario().getId())
                .usuarioNome(transferencia.getUsuario().getNome())
                .itens(itensDTO)
                .createdAt(transferencia.getCreatedAt())
                .build();
    }
}