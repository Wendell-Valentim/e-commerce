package com.io.github.wendellvalentim.mspedido.repository;

import com.io.github.wendellvalentim.mspedido.model.ItemPedido;
import com.io.github.wendellvalentim.mspedido.model.Pedido;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class PedidoSpecs {

    public static Specification<Pedido> codPedidoLike(String codPedido) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("codPedido")),
                "%" + codPedido.toUpperCase() + "%");
    }

    public static Specification<Pedido> nomeProdutoLike(String nomeProduto) {
        return (root, query, cb) -> {
            if (nomeProduto == null || nomeProduto.isBlank()) return null;

            Join<Pedido, ItemPedido> joinItens = root.join("items");
            query.distinct(true);
            return cb.like(cb.upper(joinItens.get("nomeProduto")), "%" + nomeProduto.toUpperCase() + "%");
        };
    }

    public static Specification<Pedido> dataPedidoEqual(LocalDate dataBusca) {
        return  (root, query, cb) -> {
            if (dataBusca == null) return null;

            LocalDateTime inicioData = dataBusca.atStartOfDay();

            LocalDateTime fimDia = dataBusca.atTime(LocalTime.MAX);

            return cb.between(root.get("dataPedido"), inicioData, fimDia);
        };
    }

}
