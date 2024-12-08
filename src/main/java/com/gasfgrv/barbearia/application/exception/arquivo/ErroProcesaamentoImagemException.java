package com.gasfgrv.barbearia.application.exception.arquivo;

public class ErroProcesaamentoImagemException extends RuntimeException {

    public ErroProcesaamentoImagemException(String mensagem) {
        super("Erro ao processar imagem: " + mensagem);
    }

}
