package com.io.github.wendellvalentim.msproduto.repository;

import com.io.github.wendellvalentim.msproduto.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
}
