package com.io.github.wendellvalentim.msproduto.infra.mqueue;

import com.io.github.wendellvalentim.msproduto.event.PedidoCriadoEvent;
import com.io.github.wendellvalentim.msproduto.model.Produto;
import com.io.github.wendellvalentim.msproduto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProdutoSubscriber {
    private final ProdutoService produtoService;

    @RabbitListener(queues = "${mq.queues.produto.v1.pedido-criado.estoque-baixa}")
    public void receberSolicitacaoBaixarEstoque(@Payload PedidoCriadoEvent event){

        System.out.println("Recebido pedido para o produto: " + event.produtoId());

        // Passa o produto que você buscou na linha de cima como 2º parâmetro
        Produto produto = produtoService.buscar(event.produtoId());
        produtoService.baixarEstoquePorPedido(event.produtoId(), produto);
    }
}
