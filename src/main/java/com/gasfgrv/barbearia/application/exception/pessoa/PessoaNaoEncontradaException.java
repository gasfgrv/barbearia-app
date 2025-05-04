package com.gasfgrv.barbearia.application.exception.pessoa;

public class PessoaNaoEncontradaException extends RuntimeException {

    public PessoaNaoEncontradaException() {
        super("Pessoa não encotrada ou com os dados inativos");
    }

}
