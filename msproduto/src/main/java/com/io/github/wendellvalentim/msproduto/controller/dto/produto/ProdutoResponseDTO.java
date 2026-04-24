package com.io.github.wendellvalentim.msproduto.controller.dto.produto;

import com.io.github.wendellvalentim.msproduto.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "Reposta Produto")
public record ProdutoResponseDTO (

                                 String nome,

                                  BigDecimal preco,

                                  Integer quantidade,

                                  Status status,

                                  @Schema(name = "Codigo do produto")
                                  String codProduto){
}
