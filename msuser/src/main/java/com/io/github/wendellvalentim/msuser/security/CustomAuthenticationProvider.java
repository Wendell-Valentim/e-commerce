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

        System.out.println("\n=== [PROVIDER] 🔍 INTERCEPTANDO TENTATIVA DE LOGIN ===");
        System.out.println("[PROVIDER] Login digitado no formulário: " + login);

        User usuarioEncontrado = userService.obterUserComPermissoes(login);

        if (usuarioEncontrado == null) {
            System.out.println("❌ [PROVIDER ERRO] Usuário '" + login + "' não foi encontrado no banco!");
            throw new BadCredentialsException("Usuário ou senha inválidos.");
        }

        System.out.println("✅ [PROVIDER] Usuário achado no banco! ID: " + usuarioEncontrado.getId());
        System.out.println("[PROVIDER] Roles brutas vindas da tabela user_roles: " + usuarioEncontrado.getRoles());

        boolean senhasBatem = passwordEncoder.matches(senhaDigitada, usuarioEncontrado.getPassword());

        System.out.println("[PROVIDER] A senha bate com o hash do banco? " + (senhasBatem ? "SIM" : "NÃO"));

        if (!senhasBatem) {
            throw new BadCredentialsException("Usuário ou senha inválidos.");
        }

        System.out.println("🔄 [PROVIDER] Enviando usuário para o Mapper converter em DTO...");
        IdentificacaoUsuario identificacaoUsuario = mapper.toDTO(usuarioEncontrado);

        System.out.println("[PROVIDER RESULTADO MAPPER] O DTO gerado ficou nulo? " + (identificacaoUsuario == null ? "SIM 🚨" : "NÃO"));
        if (identificacaoUsuario != null) {
            System.out.println("[PROVIDER RESULTADO MAPPER] E-mail no DTO: " + identificacaoUsuario.email());
            System.out.println("[PROVIDER RESULTADO MAPPER] Roles repassadas ao DTO: " + identificacaoUsuario.roles());
        }
        System.out.println("🚀 [PROVIDER] Instanciando a classe CustomAuthentication e entregando pro Spring...\n");
        return new CustomAuthentication(identificacaoUsuario);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
