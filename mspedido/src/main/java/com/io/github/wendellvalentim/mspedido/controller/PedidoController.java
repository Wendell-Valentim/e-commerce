package com.io.github.wendellvalentim.mspedido.controller;

import com.io.github.wendellvalentim.mspedido.controller.dto.Pedido.PedidoRequestDTO;
import com.io.github.wendellvalentim.mspedido.controller.dto.Pedido.PedidoResponseDTO;
import com.io.github.wendellvalentim.mspedido.controller.generic.GenericController;
import com.io.github.wendellvalentim.mspedido.mapper.PedidoMapper;
import com.io.github.wendellvalentim.mspedido.model.Pedido;
import com.io.github.wendellvalentim.mspedido.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController implements GenericController {

    private final PedidoService service;
    private final PedidoMapper mapper;

    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody @Valid PedidoRequestDTO requestDTO) {
        Pedido pedido = service.salvar(requestDTO);
        URI location = gerarHeaderLocation(pedido.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscar(@PathVariable UUID id) {
        PedidoResponseDTO dto = mapper.toDTO(service.buscarPorId(id));

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/cancelar/{id}")
    public ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable UUID id) {

        PedidoResponseDTO pedidoCancelado = mapper.toDTO(service.solicitarCancelamentoPedido(id));

        return ResponseEntity.ok(pedidoCancelado);
    }

}
