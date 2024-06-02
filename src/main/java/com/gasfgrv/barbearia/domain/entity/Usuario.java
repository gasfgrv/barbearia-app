package com.gasfgrv.barbearia.domain.entity;

import lombok.Data;

@Data
public class Usuario {

    private String login;
    private String senha;
    private Perfil perfil;

}
