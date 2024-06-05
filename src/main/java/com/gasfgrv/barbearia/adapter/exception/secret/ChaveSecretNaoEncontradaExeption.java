package com.gasfgrv.barbearia.adapter.exception.secret;

public class ChaveSecretNaoEncontradaExeption extends RuntimeException {

    public ChaveSecretNaoEncontradaExeption(String chave, String nome) {
        super(String.format("Erro ao consultar secret: a chave '%s' não existe no secret '%s'", chave, nome));
    }

}
