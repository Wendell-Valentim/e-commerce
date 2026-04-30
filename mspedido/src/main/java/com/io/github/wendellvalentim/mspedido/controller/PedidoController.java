package com.io.github.wendellvalentim.mspedido.controller;

import com.io.github.wendellvalentim.mspedido.controller.dto.PedidoRequestDTO;
import com.io.github.wendellvalentim.mspedido.model.Pedido;
import com.io.github.wendellvalentim.mspedido.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody PedidoRequestDTO requestDTO) {
        Pedido pedido = service.salvar(requestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

}
