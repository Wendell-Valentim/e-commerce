package com.io.github.wendellvalentim.msproduto.mappers;

import com.io.github.wendellvalentim.msproduto.controller.dto.produto.*;
import com.io.github.wendellvalentim.msproduto.model.Produto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Produto updateToEntity(ProdutoUpdateDTO dto, @MappingTarget Produto produto);

    Produto toEntity(ProdutoCreatedDTO dto);

    ProdutoResponseDTO toDTO(Produto produto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Produto estoqueToEntity(EstoqueUpdateDTO dto);

    EstoqueResponseDTO estoqueToDTO(Produto produto);
}

