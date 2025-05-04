package com.gasfgrv.barbearia.adapter.controller.pessoa;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class PessoaResponse implements Serializable {

    private String nome;
    private String cpf;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
    private String login;
    private String celular;

}
