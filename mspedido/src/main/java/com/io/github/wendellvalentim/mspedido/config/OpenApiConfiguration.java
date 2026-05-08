package com.io.github.wendellvalentim.mspedido.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "MsPedido API",
                version = "v1",
                contact = @Contact(
                        name = "Wendell Valentim",
                        email = "wendellldasiilva8@gmail.com"
                )
        )
)
public class OpenApiConfiguration {
}
