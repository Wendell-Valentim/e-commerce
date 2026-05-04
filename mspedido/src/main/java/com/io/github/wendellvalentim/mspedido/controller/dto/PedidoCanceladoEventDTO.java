package com.io.github.wendellvalentim.mspedido.controller.dto;

import java.util.UUID;

public record PedidoCanceladoEventDTO(UUID produtoId,
                                      Integer quantidade) {
}
