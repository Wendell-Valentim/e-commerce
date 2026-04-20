package com.io.github.wendellvalentim.msproduto.model;

import com.io.github.wendellvalentim.msproduto.Status;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "tb_produtos")
@EntityListeners(AuditingEntityListener.class)
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(unique = true, nullable = false)
    private String codProduto;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    @Column(name = "data_cadastro")
    private LocalDateTime dataCriacao;
}
