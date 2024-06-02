package com.gasfgrv.barbearia.port.secret;

public interface SecretPort {

    String obterSecret(String nomeSecret);

    String obterSecret(String nome, String chave);

}
