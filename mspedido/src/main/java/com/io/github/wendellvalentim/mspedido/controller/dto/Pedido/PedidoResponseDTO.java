package com.io.github.wendellvalentim.mspedido.controller.dto.Pedido;

import com.io.github.wendellvalentim.mspedido.controller.dto.ItemPedido.ItemPedidoResponseDTO;
import com.io.github.wendellvalentim.mspedido.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(String codigoPedido,
                                StatusPedido status,
                                LocalDateTime dataPedido,
                                List<ItemPedidoResponseDTO> items,
                                BigDecimal total) {
}
