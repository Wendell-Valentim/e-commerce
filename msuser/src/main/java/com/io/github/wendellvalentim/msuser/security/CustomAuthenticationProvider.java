package com.io.github.wendellvalentim.msuser.security;

import com.io.github.wendellvalentim.msuser.mapper.UserMapper;
import com.io.github.wendellvalentim.msuser.model.User;
import com.io.github.wendellvalentim.msuser.security.dto.IdentificacaoUsuario;
import com.io.github.wendellvalentim.msuser.service.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;


    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String senhaDigitada = authentication.getCredentials().toString();

        User usuarioEncontrado = userService.obterUserComPermissoes(login);

        if (usuarioEncontrado == null) {
            throw new BadCredentialsException("Usuário ou senha inválidos.");
        }

        boolean senhasBatem = passwordEncoder.matches(senhaDigitada, usuarioEncontrado.getPassword());

        if (!senhasBatem) {
            throw new BadCredentialsException("Usuário ou senha inválidos.");
        }

        IdentificacaoUsuario identificacaoUsuario = mapper.toDTO(usuarioEncontrado);
        return new CustomAuthentication(identificacaoUsuario);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
