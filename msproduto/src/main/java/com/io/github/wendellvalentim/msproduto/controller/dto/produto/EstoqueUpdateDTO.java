package com.io.github.wendellvalentim.msproduto.controller.dto.produto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EstoqueUpdateDTO(

        @NotNull
        Integer quantidade) {
}
