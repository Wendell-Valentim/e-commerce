package com.io.github.wendellvalentim.msuser.controllers;

import com.io.github.wendellvalentim.msuser.controllers.common.generic.GenericController;
import com.io.github.wendellvalentim.msuser.controllers.dto.UserRequestDTO;
import com.io.github.wendellvalentim.msuser.entities.UserEntity;
import com.io.github.wendellvalentim.msuser.mapper.UserMapper;
import com.io.github.wendellvalentim.msuser.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
