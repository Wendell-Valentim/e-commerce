package com.io.github.wendellvalentim.mspedido.controller.dto.Pedido;

import java.util.UUID;

public record PedidoCanceladoEventDTO(UUID produtoId,
                                      Integer quantidade) {
}
