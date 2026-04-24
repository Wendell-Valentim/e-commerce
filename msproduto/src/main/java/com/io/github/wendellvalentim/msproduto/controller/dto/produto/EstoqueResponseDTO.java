package com.io.github.wendellvalentim.msproduto.controller.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Resposta Estoque")
public record EstoqueResponseDTO(String nome,
                                 @Schema(name = "Codigo do Produto")
                                 String codProduto,
                                 Integer quantidade) {
}
