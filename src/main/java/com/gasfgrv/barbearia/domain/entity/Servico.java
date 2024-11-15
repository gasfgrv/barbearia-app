package com.gasfgrv.barbearia.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class Servico implements Serializable     {

    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int duracao;
    private boolean ativo;

}
