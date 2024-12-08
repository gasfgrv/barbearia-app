package com.gasfgrv.barbearia.application.exception.pessoa;

public class IdadeInvalidaException extends RuntimeException {

    public IdadeInvalidaException() {
        super("Idade inválida para cadastro");
    }
}
