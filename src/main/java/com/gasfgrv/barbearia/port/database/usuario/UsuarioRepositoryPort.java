package com.gasfgrv.barbearia.port.database.usuario;

import com.gasfgrv.barbearia.domain.entity.Usuario;

public interface UsuarioRepositoryPort {
    Usuario findByLogin(String login);

    void salvarUsuario(Usuario usuario);
}
