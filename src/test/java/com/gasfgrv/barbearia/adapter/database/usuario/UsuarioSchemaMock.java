package com.gasfgrv.barbearia.adapter.database.usuario;

import com.gasfgrv.barbearia.adapter.database.perfil.PerfilSchemaMock;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuarioSchemaMock {

    public UsuarioSchema montarUsuarioSchema() {
        UsuarioSchema usuarioSchema = new UsuarioSchema();
        usuarioSchema.setLogin("teste@teste.com");
        usuarioSchema.setSenha("123456");
        usuarioSchema.setPerfil(PerfilSchemaMock.montarPerfilSchema());
        return usuarioSchema;
    }

}