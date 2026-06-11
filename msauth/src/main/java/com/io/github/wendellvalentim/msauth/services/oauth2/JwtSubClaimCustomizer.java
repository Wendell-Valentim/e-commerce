package com.io.github.wendellvalentim.msauth.services.oauth2;

import com.io.github.wendellvalentim.msauth.entities.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtSubClaimCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
    @Override
    public void customize(JwtEncodingContext context) {
        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            Authentication principal = context.getPrincipal();

            Object principalObj = principal.getPrincipal();
            if (principalObj instanceof UserEntity user) {
                context.getClaims().subject(user.getSub());
            }

            Set<String> roles = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());

            context.getClaims().claim("roles", roles);

            Set<String> scopes = context.getAuthorizedScopes();
            context.getClaims().claim("scope", scopes);
        }
    }
}
