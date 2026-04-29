package com.io.github.wendellvalentim.mspedido.service;

import com.io.github.wendellvalentim.mspedido.controller.dto.PedidoRequestDTO;
import com.io.github.wendellvalentim.mspedido.enums.StatusPedido;
import com.io.github.wendellvalentim.mspedido.infra.ProdutoResourceClient;
import com.io.github.wendellvalentim.mspedido.mapper.ItemPedidoMapper;
import com.io.github.wendellvalentim.mspedido.model.ItemPedido;
import com.io.github.wendellvalentim.mspedido.model.Pedido;
import com.io.github.wendellvalentim.mspedido.model.produto.ProdutoResponseDTO;
import com.io.github.wendellvalentim.mspedido.repository.ItemPedidoRepository;
import com.io.github.wendellvalentim.mspedido.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoResourceClient produtoResourceClient;
    private final ItemPedidoMapper mapper;

    @Transactional
    public Pedido salvar (PedidoRequestDTO request) {
        Pedido pedido = new Pedido();
        pedido.setCodigoPedido(UUID.randomUUID().toString());
        pedido.setStatus(StatusPedido.RECEBIDO);
        List<ItemPedido> listaDeItens = request.itens().stream().map(itemDTO -> {

            ResponseEntity<ProdutoResponseDTO> response = produtoResourceClient.getProdutosById(itemDTO.produtoId());
            ProdutoResponseDTO produtoData = response.getBody();

            if (produtoData.estoqueDisponivel() < itemDTO.quantidade()) {
                throw new RuntimeException("Estoque insuficiente para o produto: " + produtoData.nomeProduto());
            }

            ItemPedido itemEntity = mapper.toEntity(produtoData);

            itemEntity.setQuantidade(itemDTO.quantidade());

            itemEntity.setPedido(pedido);

            itemEntity.setSubTotal(itemEntity.getPrecoUnitario().multiply(BigDecimal.valueOf(itemEntity.getQuantidade())));

            return itemEntity;
        }).toList();

        BigDecimal totalPedido = listaDeItens.stream()
                .map(ItemPedido::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setItems(listaDeItens);
        pedido.setTotal(totalPedido);

        return  pedidoRepository.save(pedido);
    }
}
