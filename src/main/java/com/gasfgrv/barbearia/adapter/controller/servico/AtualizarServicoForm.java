package com.gasfgrv.barbearia.adapter.controller.servico;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AtualizarServicoForm {

    @Size(max = 255)
    private String nome;

    @Size(max = 255)
    private String descricao;

    private BigDecimal preco;

    private int duracao;

}
