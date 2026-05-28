package com.io.github.wendellvalentim.msauth.services.oauth2;

import com.io.github.wendellvalentim.msauth.entities.UserEntity;
import com.io.github.wendellvalentim.msauth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CustomOidcUserInfoMapper implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {

    private final UserRepository userRepository;

    @Override
    public OidcUserInfo apply(OidcUserInfoAuthenticationContext context) {
        OAuth2Authorization authorization = context.getAuthorization();
        Set<String> scopes = context.getAccessToken().getScopes();
        String email = authorization.getPrincipalName();

        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN,
                        "User no long existes, token is stale", null))
        );

        OidcUserInfo.Builder builder = OidcUserInfo.builder()
                .subject(user.getSub());

        if (scopes.contains(OidcScopes.PROFILE)) {
            if (user.getName() != null) builder.name(user.getName());
            if (user.getPicture() != null) builder.picture(user.getPicture());
        }
        if (scopes.contains(OidcScopes.EMAIL)) {
            builder.email(user.getEmail());
            builder.emailVerified(user.isEmailVerified());
        }

        return builder.build();


    }
}
