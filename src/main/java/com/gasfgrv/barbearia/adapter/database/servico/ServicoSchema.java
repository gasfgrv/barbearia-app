package com.gasfgrv.barbearia.adapter.database.servico;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "servico")
public class ServicoSchema implements Serializable {

    @Id
    private UUID id;

    private String nome;

    private String descricao;

    private BigDecimal preco;

    private int duracao;

    private boolean ativo;

}
