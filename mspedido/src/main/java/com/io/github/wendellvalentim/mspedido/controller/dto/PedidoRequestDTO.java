package com.io.github.wendellvalentim.mspedido.controller.dto;

import java.util.List;

public record PedidoRequestDTO(List<ItemPedidoRequestDTO> itens) {
}
