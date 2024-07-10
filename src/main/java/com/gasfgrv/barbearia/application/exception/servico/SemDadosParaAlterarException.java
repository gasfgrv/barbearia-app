package com.gasfgrv.barbearia.application.exception.servico;

public class SemDadosParaAlterarException extends RuntimeException {

    public SemDadosParaAlterarException() {
        super("Não houve nenhuma mudança para efetivar a alteração");
    }

}
