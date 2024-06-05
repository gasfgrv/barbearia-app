package com.gasfgrv.barbearia.application.exception.email;

public class EnvioEmailException extends RuntimeException {

    public EnvioEmailException(Throwable throwable) {
        super("Erro ao enviar e-mail", throwable);
    }

}
