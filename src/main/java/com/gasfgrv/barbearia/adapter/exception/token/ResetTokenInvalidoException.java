package com.gasfgrv.barbearia.adapter.exception.token;

public class ResetTokenInvalidoException extends RuntimeException {

    public ResetTokenInvalidoException() {
        super("Não é possível alterar a senha: token não existe ou a está expirado");
    }

}
