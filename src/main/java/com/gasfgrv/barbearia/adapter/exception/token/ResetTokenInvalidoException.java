package com.gasfgrv.barbearia.adapter.exception.token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ResetTokenInvalidoException extends RuntimeException {

    public ResetTokenInvalidoException(TipoErro tipoErro) {
        super("Não é possível alterar a senha: " + tipoErro.getMensagem());
    }

    @Getter
    @RequiredArgsConstructor
    public enum TipoErro {
        INEXISTENTE("token não existe"),
        EXPIRADO("token expirado");

        private final String mensagem;
    }

}
