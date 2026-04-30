package com.io.github.wendellvalentim.mspedido.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {
    @Value("${mq.queues.produto.v1.pedido-criado.estoque-baixa: produto.v1.pedido-criado.estoque-baixa}")
    private String baixaEstoqueFila;

    @Bean
    public Queue queueProduto() {
        return new Queue(baixaEstoqueFila, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
