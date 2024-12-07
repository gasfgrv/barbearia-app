package com.gasfgrv.barbearia.domain.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class Arquivo {

    private UUID idImagem;
    private String nome;
    private String tipo;
    private byte[] bytes;

}
