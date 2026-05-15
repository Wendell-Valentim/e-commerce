package com.io.github.wendellvalentim.msuser.controller;

import com.io.github.wendellvalentim.msuser.model.Client;
import com.io.github.wendellvalentim.msuser.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void salvar(@RequestBody Client client) {
        clientService.salvar(client);
    }
}
