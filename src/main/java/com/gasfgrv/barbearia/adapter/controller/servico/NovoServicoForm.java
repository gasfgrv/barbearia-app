package com.gasfgrv.barbearia.adapter.controller.servico;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class NovoServicoForm {

    @NotBlank
    @Size(min = 10, max = 255)
    private String nome;

    @NotBlank
    @Size(min = 10, max = 255)
    private String descricao;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal preco;

    @NotNull
    @Min(value = 10)
    private int duracao;

}
