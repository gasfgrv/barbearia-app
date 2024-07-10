package com.gasfgrv.barbearia.application.exception.servico;

public class ServicoDesativadoExeception extends RuntimeException {

    public ServicoDesativadoExeception() {
        super("Serviço já se encontra desativado");
    }

}
