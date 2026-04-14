package com.io.github.wendellvalentim.msproduto.controller;

import com.io.github.wendellvalentim.msproduto.controller.common.generic.GenericController;
import com.io.github.wendellvalentim.msproduto.controller.dto.produto.ProdutoCreatedDTO;
import com.io.github.wendellvalentim.msproduto.controller.dto.produto.ProdutoUpdateDTO;
import com.io.github.wendellvalentim.msproduto.controller.dto.produto.ProdutoResponseDTO;
import com.io.github.wendellvalentim.msproduto.mappers.ProdutoMapper;
import com.io.github.wendellvalentim.msproduto.model.Produto;
import com.io.github.wendellvalentim.msproduto.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController implements GenericController {

    private final ProdutoService produtoService;
    private final ProdutoMapper mapper;

    @PostMapping
    public ResponseEntity<Void> salvar (@Valid @RequestBody ProdutoCreatedDTO request) {
        Produto produto = produtoService.salvar(request);
        URI location = gerarHeaderLocation(produto.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ProdutoResponseDTO> obterDetalhes (@PathVariable("id")UUID id){
       var produtoDTO = mapper.toDTO(produtoService.buscar(id));

       return ResponseEntity.ok(produtoDTO);
    }

    @PatchMapping("{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable("id") UUID id,
                                                        @Valid @RequestBody ProdutoUpdateDTO request) {
        Produto prodAtualizado = produtoService.atualizar(id,request);

        return ResponseEntity.ok(mapper.toDTO(prodAtualizado));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") UUID id) {
        produtoService.deletar(id);
        return ResponseEntity.ok().build();
    }
}
