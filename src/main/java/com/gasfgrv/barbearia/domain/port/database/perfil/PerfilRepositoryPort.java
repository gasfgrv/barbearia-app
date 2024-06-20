package com.gasfgrv.barbearia.domain.port.database.perfil;

import com.gasfgrv.barbearia.domain.entity.Perfil;

public interface PerfilRepositoryPort {

    Perfil findByNome(String nome);

}
