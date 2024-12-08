package com.gasfgrv.barbearia.adapter.controller.pessoa;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class CadastroClienteForm implements Serializable {

    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private UsuarioCadastroForm usuario;
    private String celular;

}
