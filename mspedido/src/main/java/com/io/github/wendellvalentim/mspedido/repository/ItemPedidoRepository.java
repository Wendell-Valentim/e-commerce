package com.io.github.wendellvalentim.mspedido.repository;

import com.io.github.wendellvalentim.mspedido.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, UUID> {
}
