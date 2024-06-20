package com.gasfgrv.barbearia.adapter.exception.email;

public class EnvioEmailException extends RuntimeException {

    public EnvioEmailException(Throwable throwable) {
        super("Erro ao enviar e-mail", throwable);
    }

}
