package com.io.github.wendellvalentim.msproduto.controller.dto.produto;

import com.io.github.wendellvalentim.msproduto.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdutoCreatedDTO (

                                 @NotNull
                                 String nome,

                                 @NotNull
                                 @Positive(message = "O preço deve ser maior que 0!")
                                 BigDecimal preco,

                                 @NotNull
                                 @Min(value = 1, message = "A quantidade não pode ser menor que 1!" )
                                 int quantidade,

                                 @NotNull
                                 String cod_prod,

                                 @NotNull(message = "O Status deve conter DISPONIVEL OU INDISPONIVEL!")
                                 Status status){
}
