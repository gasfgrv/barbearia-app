package com.gasfgrv.barbearia.domain.port.email;

import com.gasfgrv.barbearia.domain.entity.Usuario;

public interface EmailPort {

    void enviarResetToken(Usuario usuario, String url);

}
