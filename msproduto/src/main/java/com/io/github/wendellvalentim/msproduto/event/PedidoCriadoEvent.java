package com.io.github.wendellvalentim.msproduto.event;

import java.util.UUID;

public record PedidoCriadoEvent(UUID produtoId,
                                Integer quantidade) {
}
