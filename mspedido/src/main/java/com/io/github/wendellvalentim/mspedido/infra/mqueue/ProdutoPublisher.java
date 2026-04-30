package com.io.github.wendellvalentim.mspedido.infra.mqueue;

import com.io.github.wendellvalentim.mspedido.event.PedidoCriadoEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProdutoPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queueProduto;

    public void abaixarEstoqueProduto(PedidoCriadoEvent event){

        rabbitTemplate.convertAndSend(queueProduto.getName(), event);
    }

}
