package com.io.github.wendellvalentim.mspedido.controller.dto;

import java.util.UUID;

public record ItemPedidoRequestDTO(UUID produtoId,
                                   Integer quantidade) {
}
