package com.gasfgrv.barbearia.application.exception.usuario;

public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException() {
        super("Usuario não encontrado");
    }

}
