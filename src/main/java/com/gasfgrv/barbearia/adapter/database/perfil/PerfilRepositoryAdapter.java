package com.gasfgrv.barbearia.adapter.database.perfil;

import com.gasfgrv.barbearia.domain.entity.Perfil;
import com.gasfgrv.barbearia.domain.port.database.perfil.PerfilRepositoryPort;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PerfilRepositoryAdapter implements PerfilRepositoryPort {

    private final PerfilJpaRepository repository;
    private final Mapper<GrantedAuthority, Perfil> mapper;

    @Override
    public Perfil findByNome(String nome) {
        log.info("Obtendo dados do perfil: {}", nome);
        return repository.findByNome(nome.toUpperCase())
                .map(mapper::map)
                .orElse(null);
    }

}
