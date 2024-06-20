package com.gasfgrv.barbearia.domain.entity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PerfilMock {

    public Perfil montarPerfil() {
        Perfil perfil = new Perfil();
        perfil.setId(1);
        perfil.setNome("TESTE");
        return perfil;
    }

}
