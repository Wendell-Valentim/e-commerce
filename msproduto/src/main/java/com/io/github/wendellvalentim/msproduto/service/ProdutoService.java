package com.io.github.wendellvalentim.msproduto.service;

import com.io.github.wendellvalentim.msproduto.controller.dto.produto.ProdutoCreatedDTO;
import com.io.github.wendellvalentim.msproduto.controller.dto.produto.ProdutoUpdateDTO;
import com.io.github.wendellvalentim.msproduto.exceptions.ProdutoNaoEncontradoException;
import com.io.github.wendellvalentim.msproduto.mappers.ProdutoMapper;
import com.io.github.wendellvalentim.msproduto.model.Produto;
import com.io.github.wendellvalentim.msproduto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public Produto salvar(ProdutoCreatedDTO dto) {

        return produtoRepository.save(produtoMapper.toEntity(dto));
    }

    public Produto buscar(UUID id) {
        return  produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado!"));
    }


    public Produto atualizar(UUID id, ProdutoUpdateDTO dto) {
        Produto produto = buscar(id);

        produtoMapper.updateToEntity(dto, produto);

        return produtoRepository.save(produto);

    }

    public void deletar (UUID id) {
        Produto produto = buscar(id);
        produtoRepository.delete(produto);
    }



}
