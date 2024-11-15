package com.gasfgrv.barbearia.application.exception.bucket;

public class BucketException extends RuntimeException {

    public BucketException(String mensagem) {
        super("Erro ao fazer upload ao bucket: " + mensagem);
    }

}
