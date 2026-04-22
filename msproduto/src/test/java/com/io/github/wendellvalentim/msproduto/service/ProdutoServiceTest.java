package com.io.github.wendellvalentim.msproduto.service;


import com.io.github.wendellvalentim.msproduto.Status;
import com.io.github.wendellvalentim.msproduto.controller.dto.produto.ProdutoCreatedDTO;
import com.io.github.wendellvalentim.msproduto.exceptions.CodProdExists;
import com.io.github.wendellvalentim.msproduto.mappers.ProdutoMapper;
import com.io.github.wendellvalentim.msproduto.model.Produto;
import com.io.github.wendellvalentim.msproduto.repository.ProdutoRepository;
import com.io.github.wendellvalentim.msproduto.validators.ProdutoValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static  org.assertj.core.api.Assertions.*;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
 class ProdutoServiceTest {

    Produto produto;
    ProdutoCreatedDTO dto;

    @InjectMocks
    ProdutoService service;

    @Mock
    ProdutoValidator validator;

    @Mock
    ProdutoMapper mapper;

    @Mock
    ProdutoRepository repository;


    @BeforeEach
    void setup() {
        UUID id = UUID.fromString("e092e7ea-6223-4f93-8a17-942df711774c");
        dto = new ProdutoCreatedDTO("Televisao", new BigDecimal(3000), 10,"COD123", Status.DISPONIVEL);

        produto = new Produto();
        produto.setId(id);
        produto.setNome("Nome do Produto");
        produto.setPreco(new BigDecimal(3000));
        produto.setCodProduto("COD123");
    }

    @Test
    void deveSalvarUmProduto() {
        when(mapper.toEntity(dto)).thenReturn(produto);

        when(repository.save(any(Produto.class))).thenReturn(produto);

        var produtoSalvo = service.salvar(dto);

        assertThat(produtoSalvo).isNotNull();
        assertThat(produtoSalvo.getPreco()).isEqualTo(new BigDecimal(3000));

        verify(repository, times(1)).save(any());
        verify(mapper, times(1)).toEntity(any());

        System.out.println(produto.getId());
    }

    @Test
    void deveDarErroAoSalvarUmProdutoComOMesmoCodigo() {
        when(mapper.toEntity(dto)).thenReturn(produto);

        doThrow(new CodProdExists(("Codigo do produto ja cadastrado!")))
                .when(validator).validar(any(Produto.class));

        assertThrows(CodProdExists.class, () -> {
            service.salvar(dto);
        });

    verify(repository, never()).save(any());

    }

    @Test
    void deveRetornarUmProdutoQuandoIdExistir() {
        when(repository.findById(produto.getId())).thenReturn(Optional.of(produto));

        var produtoEncontrado = service.buscar(produto.getId());

        assertThat(produtoEncontrado).isEqualTo(produto);

        verify(repository).findById(produto.getId());
    }
}
