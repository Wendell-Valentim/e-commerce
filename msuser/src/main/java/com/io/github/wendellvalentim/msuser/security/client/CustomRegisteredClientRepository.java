package com.io.github.wendellvalentim.msuser.security.client;

import com.io.github.wendellvalentim.msuser.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientService clientService;
    private final TokenSettings tokenSettings;
    private final ClientSettings clientSettings;

    @Override
    public void save(RegisteredClient registeredClient) {

    }

    @Override
    public RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        System.out.println("====== INICIANDO VERIFICAÇÃO DE CLIENTE NO OAUTH2 ======");
        System.out.println("Client solicitado pelo Postman: " + clientId);
        var client = clientService.obterPorClientId(clientId);

        if(client == null) {
            System.out.println("ALERTA: Cliente não foi encontrado no banco de dados!");
            return null;
        }

        System.out.println("Cliente achado no banco! URL de Redirecionamento cadastrada: " + client.getRedirectURI());
        System.out.println("Escopos brutos vindos do banco: " + client.getScope());

        // 1. Instancia o Builder com os dados básicos
        var builder = RegisteredClient
                .withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .redirectUri(client.getRedirectURI())
                // 💡 Garante os métodos de autenticação necessários para o Postman
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                // 💡 Garante os três fluxos fundamentais do ciclo do Authorization Code
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .tokenSettings(tokenSettings)
                .clientSettings(clientSettings);

        // 2. 💡 CORREÇÃO DOS ESCOPOS: Trata a string separada por vírgula do banco
        if (client.getScope() != null && !client.getScope().isBlank()) {
            String[] scopes = client.getScope().split(",");
            for (String scope : scopes) {
                builder.scope(scope.trim()); // Adiciona cada escopo individualmente de forma limpa
            }
        }

        return builder.build();
    }
}
