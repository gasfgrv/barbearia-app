package com.gasfgrv.barbearia.port.database.perfil;

import com.gasfgrv.barbearia.domain.entity.Perfil;

public interface PerfilRepositoryPort {
    Perfil findByNome(String nome);
}
