package com.io.github.wendellvalentim.msproduto.controller.dto.produto;

import com.io.github.wendellvalentim.msproduto.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(name = "Reposta Produto")
public record ProdutoResponseDTO (  UUID id,

                                    String nome,

                                  BigDecimal preco,

                                  Integer quantidade,

                                  Status status,

                                  @Schema(name = "Codigo do produto")
                                  String codProduto){
}
