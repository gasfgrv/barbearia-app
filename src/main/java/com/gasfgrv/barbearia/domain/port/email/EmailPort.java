package com.gasfgrv.barbearia.domain.port.email;

public interface EmailPort {

    void enviarResetToken(String login, String url);

}
