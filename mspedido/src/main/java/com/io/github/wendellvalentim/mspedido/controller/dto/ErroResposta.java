package com.io.github.wendellvalentim.mspedido.controller.dto;

import java.util.List;

public record ErroResposta(int status,
                           String mensagem,
                           List<ErroCampo> erros) {
}
