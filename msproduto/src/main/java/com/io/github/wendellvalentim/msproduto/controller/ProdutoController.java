package com.io.github.wendellvalentim.msproduto.controller;

import com.io.github.wendellvalentim.msproduto.controller.common.generic.GenericController;
import com.io.github.wendellvalentim.msproduto.controller.dto.produto.*;
import com.io.github.wendellvalentim.msproduto.mappers.ProdutoMapper;
import com.io.github.wendellvalentim.msproduto.model.Produto;
import com.io.github.wendellvalentim.msproduto.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos")
public class ProdutoController implements GenericController {

    private final ProdutoService produtoService;
    private final ProdutoMapper mapper;


    @PostMapping
    @Operation(summary = "Salvar", description = "Cadastrar novo produto!")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso!"),
            @ApiResponse(responseCode = "422", description = "Erro de validação!"),
            @ApiResponse(responseCode = "400", description = "Codigo de produto ja Cadastrado!")
    })
    public ResponseEntity<Void> salvar (@Valid @RequestBody ProdutoCreatedDTO request) {
        Produto produto = produtoService.salvar(request);
        URI location = gerarHeaderLocation(produto.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @Operation(summary = "Detalhes", description = "Obter detalhes de um produto pelo ID!")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado!"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado!")

    })
    public ResponseEntity<ProdutoResponseDTO> obterDetalhes (@PathVariable("id")UUID id){
       var produtoDTO = mapper.toDTO(produtoService.buscar(id));

       return ResponseEntity.ok(produtoDTO);
    }

    @PatchMapping("{id}")
    @Operation(summary = "Atualizar", description = "Atualiza um Produto Existente!")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atualizado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado!"),
            @ApiResponse(responseCode = "400", description = "Codigo de produto ja Cadastrado!")
    })
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable("id") UUID id,
                                                        @Valid @RequestBody ProdutoUpdateDTO request) {
        Produto prodAtualizado = produtoService.atualizar(id,request);

        return ResponseEntity.ok(mapper.toDTO(prodAtualizado));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar ", description = "Deleta um produto existente!")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso!"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado!")
    })
    public ResponseEntity<Void> deletar(@PathVariable("id") UUID id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Pesquisar", description = "Pesquisar um Produto por parametro(s)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso.")

    })
    public ResponseEntity<Page<ProdutoResponseDTO>> pesquisar(@RequestParam(value = "nome", required = false)
                                                             String nome,
                                                             @RequestParam(value = "preco", required = false)
                                                             BigDecimal preco,
                                                             @RequestParam(value = "codProduto", required = false)
                                                             String cod,
                                                             @RequestParam(value = "pagina", defaultValue = "0")
                                                             Integer pagina,
                                                             @RequestParam(value = "tamanho-pagina", defaultValue = "10")
                                                             Integer tamanhoPagina) {
        Page<Produto> paginaRequest = produtoService.pesquisar(nome, cod, preco, pagina, tamanhoPagina);
        Page<ProdutoResponseDTO> resultado = paginaRequest.map(mapper::toDTO);

        return ResponseEntity.ok(resultado);
    }

    @PatchMapping("/diminuir/{id}")
    @Operation(summary = "Diminuir", description = "Diminuir o estoque de um produto!")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estoque atualizado!"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado!"),
            @ApiResponse(responseCode = "400", description = "Erro ao diminuir o estoque!")
    })
    public ResponseEntity<EstoqueResponseDTO> diminuirEstoque
            (       @PathVariable(name = "id",required = true) UUID id,
                    @Valid @RequestBody EstoqueUpdateDTO request
    ) {

        Produto produto = produtoService.baixarEstoquePorPedido(id, mapper.estoqueToEntity(request));
        EstoqueResponseDTO resultado = mapper.estoqueToDTO(produto);

        return ResponseEntity.ok(resultado);

    }

    @PatchMapping("/aumentar/{id}")
    @Operation(summary = "Aumentar", description = "Aumentar o estoque de um produto!")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estoque atualizado!"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado!"),
            @ApiResponse(responseCode = "400", description = "Erro ao aumentar o estoque!")
    })
    public ResponseEntity<EstoqueResponseDTO> aumentarEstoque (
            @PathVariable(name = "id", required = true) UUID id,
            @Valid @RequestBody EstoqueUpdateDTO request
    ) {
        Produto produto = produtoService.aumentarEstoque(id, mapper.estoqueToEntity(request));
        EstoqueResponseDTO resultado = mapper.estoqueToDTO(produto);

        return ResponseEntity.ok(resultado);

    }
}
