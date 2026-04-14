package com.io.github.wendellvalentim.msproduto.controller.dto;

import java.util.List;

public record ErroResponse(int status, String mensagem, List<ErroCampo> erros) {
}
