package com.gasfgrv.barbearia.adapter.database.usuario;

import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.port.database.usuario.UsuarioRepository;
import com.gasfgrv.barbearia.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsuarioAdapter implements UsuarioRepository {

    private final UsuarioJpaRepository repository;
    private final Mapper<UserDetails, Usuario> mapper;

    @Override
    public Usuario findByLogin(String login) {
        UserDetails usuario = repository.findByLogin(login);
        return mapper.map(usuario);
    }

}
