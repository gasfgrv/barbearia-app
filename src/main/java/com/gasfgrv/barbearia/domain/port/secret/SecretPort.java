package com.gasfgrv.barbearia.domain.port.secret;

public interface SecretPort {

    String obterSecret(String nomeSecret);

    String obterSecret(String nome, String chave);

}
