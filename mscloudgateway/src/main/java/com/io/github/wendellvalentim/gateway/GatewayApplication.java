package com.io.github.wendellvalentim.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public RouteLocator route(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/produtos/**").uri("lb://msproduto"))
				.route(r -> r.path("/pedidos/**").uri("lb://mspedido"))
				.route(r -> r.path("/users/**").uri("lb://msuser"))
				.route(r -> r.path("/clients/**").uri("lb://msauth"))

				.route(r -> r.path("/oauth2/**", "/login/**", "/userinfo", "/.well-known/**", "/css/**", "/js/**")
						.filters(f -> f
								.addRequestHeader("X-Forwarded-Host", "localhost:8085")
								.addRequestHeader("X-Forwarded-Proto", "http")
								.addRequestHeader("X-Forwarded-Port", "8085")
						)
						.uri("lb://msauth"))
				.build();
	}

}
