package com.io.github.wendellvalentim.msproduto.controller;

import com.io.github.wendellvalentim.msproduto.model.Produto;
import com.io.github.wendellvalentim.msproduto.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<Produto> salvar (@RequestBody Produto request) {
        Produto produto = produtoService.salvar(request);
        return ResponseEntity.ok(produto);
    }

    @GetMapping("{id}")
    public ResponseEntity<Produto> obterDetalhes (@PathVariable("id")UUID id){
        return produtoService.buscarPorId(id).map(produto -> ResponseEntity.ok(produto))
                .orElse(ResponseEntity.notFound().build());
    }
}
