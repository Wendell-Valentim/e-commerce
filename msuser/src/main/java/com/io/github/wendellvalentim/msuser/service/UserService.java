package com.io.github.wendellvalentim.msuser.service;

import com.io.github.wendellvalentim.msuser.entities.RoleEntity;
import com.io.github.wendellvalentim.msuser.entities.UserEntity;
import com.io.github.wendellvalentim.msuser.repositories.RoleEntityRepository;
import com.io.github.wendellvalentim.msuser.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntity salvar(UserEntity userEntity) {
        userEntity.setSub(UUID.randomUUID().toString());
        userEntity.setEmailVerified(false);

        RoleEntity rolePadrao = roleEntityRepository.findByAuthority("ROLE_CLIENT")
                .orElseThrow(() -> new RuntimeException("Erro: Role padrão ROLE_CLIENT não encontrada no banco!"));

        userEntity.getRoles().add(rolePadrao);

        String senha = passwordEncoder.encode(userEntity.getPassword());

        userEntity.setPassword(senha);

        return userRepository.save(userEntity);
    }
}
