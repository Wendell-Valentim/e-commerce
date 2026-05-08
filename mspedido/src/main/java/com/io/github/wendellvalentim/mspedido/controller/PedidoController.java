package com.io.github.wendellvalentim.mspedido.controller;

import com.io.github.wendellvalentim.mspedido.controller.dto.Pedido.PedidoRequestDTO;
import com.io.github.wendellvalentim.mspedido.controller.dto.Pedido.PedidoResponseDTO;
import com.io.github.wendellvalentim.mspedido.controller.generic.GenericController;
import com.io.github.wendellvalentim.mspedido.mapper.PedidoMapper;
import com.io.github.wendellvalentim.mspedido.model.Pedido;
import com.io.github.wendellvalentim.mspedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos")
public class PedidoController implements GenericController {

    private final PedidoService service;
    private final PedidoMapper mapper;

    @PostMapping
    @Operation(summary = "Salvar", description = "Gerar um novo pedido!")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido gerado com sucesso!"),
            @ApiResponse(responseCode = "422", description = "Erro de validação"),
            @ApiResponse(responseCode = "400", description = "Campo Invalido!"),
            @ApiResponse(responseCode = "422", description = "Erro de regra de negocio" +
            "1. Lista de itens vazia" +
            "2. Valor abaixo do minimo" +
            "3. Estoque insuficiente" +
            "4. Id do produto nulo!" +
            "5. Status não permitido para cancelamento"),


    })
    public ResponseEntity<Object> criar(@RequestBody @Valid PedidoRequestDTO requestDTO) {
        Pedido pedido = service.salvar(requestDTO);
        URI location = gerarHeaderLocation(pedido.getId());
        return ResponseEntity.created(location).build();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar", description = "Buscar um pedido por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado!"),

    })
    public ResponseEntity<PedidoResponseDTO> buscar(@PathVariable UUID id) {
        PedidoResponseDTO dto = mapper.toDTO(service.buscarPorId(id));

        return ResponseEntity.ok(dto);
    }


    @PostMapping("/cancelar/{id}")
    @Operation(summary = "Cancelar", description = "Cancelar um pedido!")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado!"),
            @ApiResponse(responseCode = "422", description = "Erro de regra de negocio" +
                    "1. Lista de itens vazia" +
                    "2. Valor abaixo do minimo" +
                    "3. Estoque insuficiente" +
                    "4. Id do produto nulo!" +
                    "5. Status não permitido para cancelamento"),

    })
    public ResponseEntity<PedidoResponseDTO> cancelar(@PathVariable UUID id) {

        PedidoResponseDTO pedidoCancelado = mapper.toDTO(service.solicitarCancelamentoPedido(id));

        return ResponseEntity.ok(pedidoCancelado);
    }


    @GetMapping
    @Operation(summary = "Buscar", description = "Buscar por parametro!")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado!"),
    })
    public ResponseEntity<Page<PedidoResponseDTO>> pesquisar(@RequestParam(value = "nome", required = false)
                                                             String nomeProduto,
                                                             @RequestParam(value = "codigo",required = false)
                                                             String codigo,
                                                             @RequestParam(value = "data",required = false)
                                                                 @DateTimeFormat(pattern = "dd/MM/yyyy")
                                                                 LocalDate dataBusca,
                                                             @RequestParam(value = "pagina", defaultValue = "0")
                                                             Integer pagina,
                                                             @RequestParam(value = "tamanho-pagina",defaultValue = "10")
                                                             Integer tamanhoPagina) {

        Page<Pedido> pedidosPage = service.pesquisa(codigo, nomeProduto, dataBusca, pagina, tamanhoPagina);
        Page<PedidoResponseDTO> resultado = pedidosPage.map(mapper::toDTO);
        return ResponseEntity.ok(resultado);
    }

}
