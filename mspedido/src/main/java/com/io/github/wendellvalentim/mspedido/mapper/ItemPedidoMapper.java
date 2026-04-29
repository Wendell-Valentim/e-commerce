package com.io.github.wendellvalentim.mspedido.mapper;

import com.io.github.wendellvalentim.mspedido.model.ItemPedido;
import com.io.github.wendellvalentim.mspedido.model.produto.ProdutoResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {

    ItemPedido toEntity(ProdutoResponseDTO dto);
}
