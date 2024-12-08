package com.gasfgrv.barbearia.adapter.controller.pessoa;

import lombok.Data;

import java.io.Serializable;

@Data
public class UsuarioCadastroForm implements Serializable {

    private String login;
    private String senha;

}
