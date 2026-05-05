package com.io.github.wendellvalentim.mspedido.controller.dto.Pedido;

import com.io.github.wendellvalentim.mspedido.controller.dto.ItemPedido.ItemPedidoRequestDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequestDTO(
        @NotNull
        List<ItemPedidoRequestDTO> itens) {
}
