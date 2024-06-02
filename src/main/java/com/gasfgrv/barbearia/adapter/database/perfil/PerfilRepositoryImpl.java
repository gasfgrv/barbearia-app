package com.gasfgrv.barbearia.adapter.database.perfil;

import com.gasfgrv.barbearia.domain.entity.Perfil;
import com.gasfgrv.barbearia.port.database.perfil.PerfilRepository;
import com.gasfgrv.barbearia.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PerfilRepositoryImpl implements PerfilRepository {

    private final PerfilJpaRepository repository;
    private final Mapper<GrantedAuthority, Perfil> mapper;

    @Override
    public Perfil findByNome(String nome) {
        GrantedAuthority authority = repository.findByNome(nome);
        return mapper.map(authority);
    }
}
