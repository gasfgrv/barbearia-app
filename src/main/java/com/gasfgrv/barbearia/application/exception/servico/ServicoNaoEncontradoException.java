package com.gasfgrv.barbearia.application.exception.servico;

public class ServicoNaoEncontradoException extends RuntimeException {

    public ServicoNaoEncontradoException() {
        super("Serviço não foi encontrado");
    }

}
