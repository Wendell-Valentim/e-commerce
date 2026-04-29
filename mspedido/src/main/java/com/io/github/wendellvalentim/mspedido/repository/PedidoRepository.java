package com.io.github.wendellvalentim.mspedido.repository;

import com.io.github.wendellvalentim.mspedido.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
}
