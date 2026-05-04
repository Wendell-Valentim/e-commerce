package com.io.github.wendellvalentim.mspedido.infra.mqueue;

import com.io.github.wendellvalentim.mspedido.event.PedidoCriadoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProdutoPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${mq.queues.produto.v1.pedido-criado.estoque-baixa}")
    private String filaBaixaEstoque;

    @Value("${mq.queues.produto.v1.pedido-cancelado.estoque-repor}")
    private String filaEstornoEstoque;

    public void abaixarEstoqueProduto(PedidoCriadoEvent event){

        rabbitTemplate.convertAndSend(filaBaixaEstoque, event);
    }

    public void aumentarEstoqueProdut(PedidoCriadoEvent event) {
        rabbitTemplate.convertAndSend(filaEstornoEstoque, event);
    }

}
