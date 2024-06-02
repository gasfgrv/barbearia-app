package com.gasfgrv.barbearia.application.service.perfil;

import com.gasfgrv.barbearia.domain.entity.Perfil;
import com.gasfgrv.barbearia.port.database.perfil.PerfilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilRepository repository;

    public Perfil obterPerfil(String perfil) {
        return repository.findByNome(perfil);
    }

}