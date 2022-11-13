package com.api.cadastroveiculos.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.util.Date;

@Entity
@Data
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String veiculo;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false, length = 4)
    private Integer ano;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Boolean vendido;

    @Column(nullable = false)
    @CreationTimestamp
    private Date created;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date updated;

    @Column(nullable = false)
    private String cor;
}
