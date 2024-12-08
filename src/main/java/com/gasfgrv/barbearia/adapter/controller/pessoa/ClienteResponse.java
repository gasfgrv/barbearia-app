package com.gasfgrv.barbearia.adapter.controller.pessoa;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ClienteResponse implements Serializable {

    private String nome;
    private LocalDate dataNascimento;
    private String celular;

}
