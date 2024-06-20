package com.gasfgrv.barbearia.adapter.database.usuario;

import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.port.database.usuario.UsuarioRepositoryPort;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository repository;
    private final Mapper<UserDetails, Usuario> mapperToDomain;
    private final Mapper<Usuario, UserDetails> mapperToUserDetails;

    @Override
    public Usuario findByLogin(String login) {
        UserDetails usuario = repository.findByLogin(login);
        return mapperToDomain.map(usuario);
    }

    @Override
    public void salvarUsuario(Usuario usuario) {
        UserDetails userDetails = mapperToUserDetails.map(usuario);
        repository.save((UsuarioSchema) userDetails);
    }

}
