package com.gasfgrv.barbearia.application.exception.pessoa;

public class PessoaExistenteException extends RuntimeException {

    public PessoaExistenteException() {
        super("Já existe alguém com o CPF cadastrado");
    }

}
