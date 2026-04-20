package com.io.github.wendellvalentim.msproduto.repository;

import com.io.github.wendellvalentim.msproduto.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID>, JpaSpecificationExecutor<Produto> {

    Optional<Produto> findByCodProduto(String codProduto);
}
