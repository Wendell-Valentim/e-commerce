package com.io.github.wendellvalentim.mspedido.infra;

import com.io.github.wendellvalentim.mspedido.model.produto.ProdutoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(value = "msproduto", path = "/produtos")
public interface ProdutoResourceClient {

    @GetMapping("{id}")  // ← sem params
    ResponseEntity<ProdutoResponseDTO> getProdutosById(@PathVariable("id") UUID id);
}