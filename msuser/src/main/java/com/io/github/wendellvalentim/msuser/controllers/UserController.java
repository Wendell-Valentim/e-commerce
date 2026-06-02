package com.io.github.wendellvalentim.msuser.controllers;

import com.io.github.wendellvalentim.msuser.controllers.common.generic.GenericController;
import com.io.github.wendellvalentim.msuser.controllers.dto.UserRequestDTO;
import com.io.github.wendellvalentim.msuser.controllers.dto.UserResponseDTO;
import com.io.github.wendellvalentim.msuser.entities.UserEntity;
import com.io.github.wendellvalentim.msuser.mapper.UserMapper;
import com.io.github.wendellvalentim.msuser.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements GenericController {

    private final UserService userService;
    private final UserMapper mapper;


    @PostMapping
    public ResponseEntity<Void> cadastrar (@RequestBody @Valid UserRequestDTO dto) {
        UserEntity entity = userService.salvar(mapper.toEntity(dto));
            URI location = gerarHeaderLocation(entity.getId());
            return ResponseEntity.created(location).build();

    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> pesquisar(@AuthenticationPrincipal Jwt jwt) {
        String sub = jwt.getSubject();

        UserEntity entity = userService.pesquisarPorSub(sub);

        return ResponseEntity.ok(mapper.toResponseDTO(entity));

    }
}
