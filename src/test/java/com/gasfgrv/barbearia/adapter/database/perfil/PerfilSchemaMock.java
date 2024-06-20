package com.gasfgrv.barbearia.adapter.database.perfil;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PerfilSchemaMock {

    public PerfilSchema montarPerfilSchema() {
        PerfilSchema perfilSchema = new PerfilSchema();
        perfilSchema.setId(1);
        perfilSchema.setNome("TESTE");
        return perfilSchema;
    }

}