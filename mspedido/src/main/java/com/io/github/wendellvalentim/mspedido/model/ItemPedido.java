package com.io.github.wendellvalentim.mspedido.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@Table(name = "tb_item_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = true)
    private UUID produtoId;

    @Column(nullable = false)
    private String nomeProduto;

    @Column(nullable = false)
    private String codProduto;

    @Column(nullable = false)
    private BigDecimal precoUnitario;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal subTotal;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @ToString.Exclude
    @JsonBackReference
    private Pedido pedido;








}
