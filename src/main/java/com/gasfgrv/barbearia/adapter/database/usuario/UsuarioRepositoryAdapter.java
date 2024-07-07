package com.gasfgrv.barbearia.adapter.database.usuario;

import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.port.database.usuario.UsuarioRepositoryPort;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository repository;
    private final Mapper<UserDetails, Usuario> mapperToDomain;
    private final Mapper<Usuario, UserDetails> mapperToUserDetails;

    @Override
    @Cacheable(value = "usuario", key = "#login")
    public Usuario findByLogin(String login) {
        log.info("Obtendo os dados do usuário na base de dados");
        return repository.findByLogin(login)
                .map(mapperToDomain::map)
                .orElse(null);
    }

    @Override
    @CacheEvict(value = "usuario", key = "#login")
    public void salvarUsuario(Usuario usuario) {
        log.info("Salvando os dados do usuário na base de dados");
        UserDetails userDetails = mapperToUserDetails.map(usuario);
        repository.save((UsuarioSchema) userDetails);
    }

}
