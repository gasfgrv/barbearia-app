package com.gasfgrv.barbearia.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Arquivo {

    private UUID idImagem;
    private String nome;
    private String tipo;
    private byte[] bytes;

}
