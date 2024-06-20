package com.gasfgrv.barbearia.application.service.perfil;

import com.gasfgrv.barbearia.domain.entity.Perfil;
import com.gasfgrv.barbearia.domain.port.database.perfil.PerfilRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilRepositoryPort repository;

    public Perfil obterPerfil(String perfil) {
        log.info("Obtendo os dados do perfil: {}", perfil);
        return repository.findByNome(perfil.toUpperCase());
    }

}
