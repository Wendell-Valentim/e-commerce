package com.io.github.wendellvalentim.msauth.services.oauth2;

import com.io.github.wendellvalentim.msauth.entities.OAuthClient;
import com.io.github.wendellvalentim.msauth.repositories.OAuthClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class CustomRegisteredClient implements RegisteredClientRepository {

    private final OAuthClientRepository clients;


    @Override
    @Transactional
    public void save(RegisteredClient registeredClient) {
        clients.save(toEntity(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        return clients.findById(UUID.fromString(id)).map(this::toDomain).orElse(null);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return clients.findByClientId(clientId).map(this::toDomain).orElse(null);
    }

    private RegisteredClient toDomain(OAuthClient entity) {
        RegisteredClient.Builder builder = RegisteredClient
                .withId(String.valueOf(entity.getId()))
                .clientId(entity.getClientId())
                .clientSecret(entity.getClientSecret())
                .clientName(entity.getClientName());

        Arrays.stream(entity.getAuthorizationGrantTypes().split(","))
                .map(String::trim)
                .forEach(t -> builder.authorizationGrantType(new AuthorizationGrantType(t)));

        Arrays.stream(entity.getClientAuthenticationMethods().split(","))
                .map(String::trim)
                .forEach(m -> builder.clientAuthenticationMethod(new ClientAuthenticationMethod(m)));

        Arrays.stream(entity.getRedirectUris().split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .forEach(builder::redirectUri);

        if (StringUtils.hasText(entity.getPostLogoutRedirectUris())) {
            Arrays.stream(entity.getPostLogoutRedirectUris().split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .forEach(builder::postLogoutRedirectUri);
        }

        Arrays.stream(entity.getScopes().split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .forEach(builder::scope);

        builder.clientSettings(ClientSettings.builder()
                .requireAuthorizationConsent(entity.isRequireConsent())
                .build());

        builder.tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofSeconds(entity.getAccessTokenTtlSeconds()))
                .refreshTokenTimeToLive(Duration.ofSeconds(entity.getRefreshTokenTtlSeconds()))
                .build());

        return builder.build();
    }

    private OAuthClient toEntity(RegisteredClient client) {
        OAuthClient entity = new OAuthClient();
        entity.setId(UUID.fromString(client.getId()));
        entity.setClientId(client.getClientId());
        entity.setClientSecret(client.getClientSecret());
        entity.setClientName(client.getClientName() != null ? client.getClientName() : client.getClientId());
        entity.setRedirectUris(String.join(",", client.getRedirectUris()));
        entity.setPostLogoutRedirectUris(
                client.getPostLogoutRedirectUris().isEmpty()
                        ? null
                        : String.join(",", client.getPostLogoutRedirectUris()));
        entity.setScopes(String.join(",", client.getScopes()));
        entity.setAuthorizationGrantTypes(client.getAuthorizationGrantTypes().stream()
                .map(AuthorizationGrantType::getValue)
                .collect(Collectors.joining(",")));
        entity.setClientAuthenticationMethods(client.getClientAuthenticationMethods().stream()
                .map(ClientAuthenticationMethod::getValue)
                .collect(Collectors.joining(",")));
        entity.setAccessTokenTtlSeconds(
                client.getTokenSettings().getAccessTokenTimeToLive().getSeconds());
        entity.setRefreshTokenTtlSeconds(
                client.getTokenSettings().getRefreshTokenTimeToLive().getSeconds());
        entity.setRequireConsent(
                client.getClientSettings().isRequireAuthorizationConsent());
        return entity;
    }
}
