package com.io.github.wendellvalentim.mspedido.event;

import java.util.UUID;

public record PedidoCriadoEvent(UUID produtoId,
                                Integer quantidade) {
}
