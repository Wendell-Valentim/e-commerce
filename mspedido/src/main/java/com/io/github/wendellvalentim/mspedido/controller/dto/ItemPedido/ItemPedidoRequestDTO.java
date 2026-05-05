package com.io.github.wendellvalentim.mspedido.controller.dto.ItemPedido;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ItemPedidoRequestDTO(
                                   @NotNull
                                   UUID produtoId,
                                   @Positive(message = "A quantidade deve ser maior que 0")
                                   Integer quantidade) {
}
