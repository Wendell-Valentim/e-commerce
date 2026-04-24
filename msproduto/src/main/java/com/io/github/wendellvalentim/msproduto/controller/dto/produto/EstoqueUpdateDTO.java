package com.io.github.wendellvalentim.msproduto.controller.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(name = "Atualização do estoque")
public record EstoqueUpdateDTO(

        @NotNull
        Integer quantidade) {
}
