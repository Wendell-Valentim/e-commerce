package com.io.github.wendellvalentim.mspedido.mapper;

import com.io.github.wendellvalentim.mspedido.controller.dto.ItemPedido.ItemPedidoResponseDTO;
import com.io.github.wendellvalentim.mspedido.controller.dto.Pedido.PedidoResponseDTO;
import com.io.github.wendellvalentim.mspedido.model.ItemPedido;
import com.io.github.wendellvalentim.mspedido.model.Pedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    PedidoResponseDTO toDTO (Pedido pedido);

    ItemPedidoResponseDTO itemPedidoToDto(ItemPedido itemPedido);
}
