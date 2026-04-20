package com.io.github.wendellvalentim.msproduto.controller.dto.produto;

import com.io.github.wendellvalentim.msproduto.Status;

import java.math.BigDecimal;

public record ProdutoResponseDTO (

                                 String nome,

                                  BigDecimal preco,

                                  Integer quantidade,

                                  Status status,

                                  String codProduto){
}
