package com.io.github.wendellvalentim.msproduto.repository;

import com.io.github.wendellvalentim.msproduto.model.Produto;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProdutoSpecs {

    public static Specification<Produto> nomeProdutoEqual(String nome) {
        return ((root, query, cb) -> cb.equal(root.get("nome"), nome));
    }

    public static Specification<Produto> codProdutoEqual(String cod_prod) {
        return (root, query, cb) -> cb.equal(root.get("cod_prod"), cod_prod);
    }

    public static Specification<Produto> precoProduto(BigDecimal preco) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("preco"), preco);
    }
}
