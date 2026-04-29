package com.io.github.wendellvalentim.mspedido.model.produto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoResponseDTO(UUID produtoId,
                                 @JsonProperty("nome")
                                 String nomeProduto,
                                 @JsonProperty("codProduto")
                                 String codProduto,
                                 @JsonProperty("preco")
                                 BigDecimal precoUnitario,
                                 @JsonProperty("quantidade")
                                 Integer estoqueDisponivel) {
}
