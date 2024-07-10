package com.gasfgrv.barbearia.adapter.controller.servico;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ServicoResponse {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int duracao;
}
