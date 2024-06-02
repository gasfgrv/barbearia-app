package com.gasfgrv.barbearia.port.database.usuario;

import com.gasfgrv.barbearia.domain.entity.Usuario;

public interface UsuarioRepository {
    Usuario findByLogin(String login);
}
