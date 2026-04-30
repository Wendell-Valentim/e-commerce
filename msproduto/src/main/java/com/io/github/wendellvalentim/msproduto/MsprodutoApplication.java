package com.io.github.wendellvalentim.msproduto;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableRabbit
public class MsprodutoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsprodutoApplication.class, args);
	}

}
