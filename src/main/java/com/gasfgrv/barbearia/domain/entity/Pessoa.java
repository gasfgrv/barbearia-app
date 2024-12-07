package com.gasfgrv.barbearia.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class Pessoa {

    private UUID id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private Usuario usuario;
    private String celular;
    private Arquivo imagem;
    private boolean ativo;

}

