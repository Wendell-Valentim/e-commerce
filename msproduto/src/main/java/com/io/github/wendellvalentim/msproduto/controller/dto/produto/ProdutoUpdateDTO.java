package com.io.github.wendellvalentim.msproduto.controller.dto.produto;

import com.io.github.wendellvalentim.msproduto.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(name = "Atualização produto")
public record ProdutoUpdateDTO(
        @NotNull
        @Positive(message = "O preço deve ser maior que 0!")
        BigDecimal preco,

        @NotNull
        @Min(value = 1, message = "A quantidade não pode ser menor que 1!" )
        int quantidade,

        @NotNull(message = "O Status deve conter DISPONIVEL OU INDISPONIVEL!")
        Status status) {
}
