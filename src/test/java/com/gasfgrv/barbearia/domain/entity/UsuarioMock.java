package com.gasfgrv.barbearia.domain.entity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UsuarioMock {

    public Usuario montarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setLogin("teste@teste.com");
        usuario.setSenha("123456");
        usuario.setPerfil(PerfilMock.montarPerfil());
        return usuario;
    }

}
