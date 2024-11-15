package com.gasfgrv.barbearia.domain.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Usuario implements Serializable {

    private String login;
    private String senha;
    private Perfil perfil;

}
