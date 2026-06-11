package com.io.github.wendellvalentim.mspedido.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
public class FeignClientConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor(OAuth2AuthorizedClientManager authorizedClientManager) {
        return requestTemplate -> {
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                    .withClientRegistrationId("mspedido-client")
                    .principal(applicationName)
                    .build();

            OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

            if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
                throw new IllegalStateException("Não foi possível obter token de acesso para " + applicationName);
            }

            requestTemplate.header("Authorization", "Bearer " + authorizedClient.getAccessToken().getTokenValue());
        };
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService) {

        OAuth2AuthorizedClientProvider provider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);

        manager.setAuthorizedClientProvider(provider);
        return manager;
    }
}