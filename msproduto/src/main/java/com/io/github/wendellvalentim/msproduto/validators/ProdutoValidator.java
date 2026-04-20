package com.io.github.wendellvalentim.msproduto.validators;

import com.io.github.wendellvalentim.msproduto.exceptions.CodProdExists;
import com.io.github.wendellvalentim.msproduto.model.Produto;
import com.io.github.wendellvalentim.msproduto.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProdutoValidator {

    private final ProdutoRepository produtoRepository;

    public void validar (Produto produto) {
        if(existeCodProd(produto)) {
            throw new CodProdExists("Codigo do produto ja cadastrado!");
        }
    }


    public boolean existeCodProd(Produto produto) {
        return produtoRepository.findByCodProduto(produto.getCodProduto())
                .map(pEncontrado -> !pEncontrado.getId().equals(produto.getId()))
                .orElse(false);

    }


}
