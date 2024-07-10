package com.gasfgrv.barbearia.application.exception.servico;

public class ServicoAtivoExeception extends RuntimeException {

    public ServicoAtivoExeception() {
        super("Serviço já se encontra ativado");
    }

}
