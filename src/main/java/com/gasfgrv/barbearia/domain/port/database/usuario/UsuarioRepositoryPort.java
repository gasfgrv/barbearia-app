package com.gasfgrv.barbearia.domain.port.database.usuario;

import com.gasfgrv.barbearia.domain.entity.Usuario;

public interface UsuarioRepositoryPort {

    Usuario findByLogin(String login);

    void salvarUsuario(Usuario usuario);

}
