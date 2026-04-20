package com.io.github.wendellvalentim.msproduto.service;

import com.io.github.wendellvalentim.msproduto.controller.dto.produto.EstoqueUpdateDTO;
import com.io.github.wendellvalentim.msproduto.controller.dto.produto.ProdutoCreatedDTO;
import com.io.github.wendellvalentim.msproduto.controller.dto.produto.ProdutoUpdateDTO;
import com.io.github.wendellvalentim.msproduto.exceptions.EstoqueInsuficienteException;
import com.io.github.wendellvalentim.msproduto.exceptions.ProdutoNaoEncontradoException;
import com.io.github.wendellvalentim.msproduto.mappers.ProdutoMapper;
import com.io.github.wendellvalentim.msproduto.model.Produto;
import com.io.github.wendellvalentim.msproduto.repository.ProdutoRepository;
import com.io.github.wendellvalentim.msproduto.validators.ProdutoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.UUID;

import static com.io.github.wendellvalentim.msproduto.repository.ProdutoSpecs.nomeProdutoEqual;
import  static com.io.github.wendellvalentim.msproduto.repository.ProdutoSpecs.codProdutoEqual;
import  static com.io.github.wendellvalentim.msproduto.repository.ProdutoSpecs.precoProduto;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;
    private final ProdutoValidator validator;

    @Transactional
    public Produto salvar(ProdutoCreatedDTO dto) {
        validator.validar(produtoMapper.toEntity(dto));
        return produtoRepository.save(produtoMapper.toEntity(dto));
    }

    public Produto buscar(UUID id) {
        return  produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado!"));
    }


    @Transactional
    public Produto atualizar(UUID id, ProdutoUpdateDTO dto) {
        Produto produto = buscar(id);
        validator.validar(produto);
        produtoMapper.updateToEntity(dto, produto);

        return produtoRepository.save(produto);

    }

    public void deletar (UUID id) {
        Produto produto = buscar(id);
        produtoRepository.delete(produto);
    }

    public Page<Produto> pesquisar (String nome, String cod_prod, BigDecimal preco, Integer pagina, Integer tamanhoPagina) {
        Specification<Produto> specs = Specification.
                where((root, query, cb) ->  cb.conjunction());

        if(StringUtils.hasText(nome)) {
            specs = specs.and(nomeProdutoEqual(nome));
        }

        if(StringUtils.hasText(cod_prod)) {
            specs = specs.and(codProdutoEqual(cod_prod));
        }

        if(preco != null) {
            specs = specs.and(precoProduto(preco));
        }

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        return produtoRepository.findAll(specs, pageRequest);
    }

    @Transactional
    public Produto baixarEstoquePorPedido(UUID id, Produto request) {
        Produto produto = buscar(id);

        if(produto.getQuantidade() < request.getQuantidade()) {
            throw new EstoqueInsuficienteException("Estoque indisponivel!");
        }

        produto.setQuantidade(produto.getQuantidade() - request.getQuantidade());
        return produtoRepository.save(produto);
    }

}
