package com.io.github.wendellvalentim.mspedido.controller.dto.ItemPedido;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(String nomeProduto,
                                    String codProduto,
                                    BigDecimal precoUnitario,
                                    Integer quantidade,
                                    BigDecimal subTotal) {
}
