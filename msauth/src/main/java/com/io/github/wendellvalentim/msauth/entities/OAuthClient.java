package com.io.github.wendellvalentim.msauth.entities;

import jakarta.persistence.*;
import lombok.Data;


import java.util.UUID;

@Entity
@Table(name = "tb_oauth_client")
@Data
public class OAuthClient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String clientId;

    // Pode ser null para public clients (sem secret)
    private String clientSecret;

    @Column(nullable = false)
    private String clientName;

    /** Redirect URIs separadas por vírgula */
    @Column(nullable = false, length = 1000)
    private String redirectUris;

    /** Post-logout redirect URIs separadas por vírgula (null = nenhuma) */
    @Column(length = 1000)
    private String postLogoutRedirectUris;

    /** Scopes separados por vírgula — ex: "openid,profile,email" */
    @Column(nullable = false, length = 500)
    private String scopes;

    /** Grant types separados por vírgula — ex: "authorization_code,refresh_token" */
    @Column(nullable = false, length = 200)
    private String authorizationGrantTypes;

    /** Métodos de autenticação do client separados por vírgula */
    @Column(nullable = false, length = 200)
    private String clientAuthenticationMethods;

    /** Validade do access token em segundos (default: 1 hora) */
    @Column(nullable = false)
    private long accessTokenTtlSeconds = 3600L;

    /** Validade do refresh token em segundos (default: 30 dias) */
    @Column(nullable = false)
    private long refreshTokenTtlSeconds = 2592000L;

    /** Se exige tela de consent para o usuário. False para apps first-party. */
    @Column(nullable = false)
    private boolean requireConsent = true;
}
