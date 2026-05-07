package com.io.github.wendellvalentim.mspedido.controller.dto.Pedido;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.io.github.wendellvalentim.mspedido.controller.dto.ItemPedido.ItemPedidoResponseDTO;
import com.io.github.wendellvalentim.mspedido.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(String codigoPedido,
                                StatusPedido status,
                                @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                                LocalDateTime dataPedido,
                                List<ItemPedidoResponseDTO> items,
                                BigDecimal total) {
}
