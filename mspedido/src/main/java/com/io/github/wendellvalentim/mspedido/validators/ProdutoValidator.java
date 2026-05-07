package com.io.github.wendellvalentim.mspedido.validators;

import com.io.github.wendellvalentim.mspedido.controller.dto.ItemPedido.ItemPedidoRequestDTO;
import com.io.github.wendellvalentim.mspedido.controller.dto.Pedido.PedidoRequestDTO;
import com.io.github.wendellvalentim.mspedido.exception.EstoqueInsuficienteException;
import com.io.github.wendellvalentim.mspedido.model.produto.ProdutoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProdutoValidator {


    public void validar(ItemPedidoRequestDTO item, ProdutoResponseDTO produtoData){
        if(validarId(item)){
            throw new IllegalArgumentException("produtoId não pode ser nulo");
        }
        if(validarEstoqueDisponivel(produtoData, item.quantidade())){
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para o produto: " + produtoData.nomeProduto());
        }
    }


    private boolean validarId(ItemPedidoRequestDTO item) {
        return item.produtoId() == null;
    }

    private boolean validarEstoqueDisponivel(ProdutoResponseDTO produtoData, Integer quantidade) {
        return produtoData.estoqueDisponivel() < quantidade;
    }

}
