package com.io.github.wendellvalentim.mspedido.controller.dto.Pedido;

import com.io.github.wendellvalentim.mspedido.controller.dto.ItemPedido.ItemPedidoRequestDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequestDTO(
        @NotNull
        @NotEmpty
        List<ItemPedidoRequestDTO> itens) {
}
