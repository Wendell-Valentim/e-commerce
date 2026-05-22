package com.io.github.wendellvalentim.msuser.security;

import com.io.github.wendellvalentim.msuser.security.dto.IdentificacaoUsuario;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomAuthentication implements Authentication {
    private final IdentificacaoUsuario identificacaoUsuario;
    private boolean authenticated = true;

    public CustomAuthentication(IdentificacaoUsuario identificacaoUsuario) {
        System.out.println("=== [CUSTOM AUTH] 🏗️ CONSTRUTOR CHAMADO ===");
        System.out.println("[CUSTOM AUTH] O DTO recebido no construtor é nulo? " + (identificacaoUsuario == null ? "SIM 🚨" : "NÃO"));
        if(identificacaoUsuario == null){
            throw new ExceptionInInitializerError("Não é possível criar um customauthentication sem a identificacao do usuario");
        }
        this.identificacaoUsuario = identificacaoUsuario;

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("=== [CUSTOM AUTH] 🛡️ SPRING PEDIU AS AUTHORITIES (ROLES) ===");

        if (this.identificacaoUsuario == null) {
            System.out.println("❌ [CUSTOM AUTH ERRO] Impedido crash 500: identificacaoUsuario está NULO!");
            return List.of();
        }

        if (this.identificacaoUsuario.roles() == null) {
            System.out.println("❌ [CUSTOM AUTH ERRO] Impedido crash 500: A lista de roles dentro do DTO está NULA!");
            return List.of();
        }

        System.out.println("[CUSTOM AUTH] Mapeando as seguintes roles para o Spring Security: " + this.identificacaoUsuario.roles());

        return this.identificacaoUsuario.roles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @Nullable Object getDetails() {
        return this.identificacaoUsuario;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return this.identificacaoUsuario;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // 💡 REMOVA O THROW! Deixe o Spring alterar o estado se ele precisar durante o ciclo de vida.
        this.authenticated = isAuthenticated;
        System.out.println("🔄 [CUSTOM AUTH] Spring alterou o estado de autenticação para: " + isAuthenticated);
    }

    @Override
    public String getName() {
        return this.identificacaoUsuario.email();
    }
}
