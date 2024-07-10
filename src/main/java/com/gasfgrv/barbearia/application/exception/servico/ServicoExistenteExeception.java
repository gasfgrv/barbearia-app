package com.gasfgrv.barbearia.application.exception.servico;

public class ServicoExistenteExeception extends RuntimeException {

    public ServicoExistenteExeception() {
        super("Serviço já existe");
    }

}
