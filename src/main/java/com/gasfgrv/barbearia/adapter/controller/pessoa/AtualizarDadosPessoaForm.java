package com.gasfgrv.barbearia.adapter.controller.pessoa;

import lombok.Data;

import java.io.Serializable;

@Data
public class AtualizarDadosPessoaForm implements Serializable {

    private String nome;
    private String celular;

}
