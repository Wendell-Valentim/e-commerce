package com.io.github.wendellvalentim.mspedido.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.io.github.wendellvalentim.mspedido.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "tb_pedido")
@EntityListeners(AuditingEntityListener.class)
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String codigoPedido;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @Column
    @CreatedDate
    private LocalDateTime dataPedido;

    @OneToMany(mappedBy = "pedido",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<ItemPedido> items;
}
