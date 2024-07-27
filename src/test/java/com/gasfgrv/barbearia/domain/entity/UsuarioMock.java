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

    public String montarTokenJWT() {
        return "ewogICJhbGciOiJIUzI1NiIKfQ" +
                ".ewogICAgInRleHQiOiAidG9rZW4gZGUgdGVzdGUiCn0" +
                ".ZXdvZ0lDSmhiR2NpT2lKSVV6STFOaUlLZlEuLmNETVZLdHFYNmhFNG12MGFGRzRnTXc4SmdZbkxQZTUwQjdYUWg0bHFDWEE";
    }

    public String montarSenhaCriptografada() {
        return "$2y$10$7TIzNabfy6J4in2DfbbGKe1ID/Cc9H6g6DVzoooMZv1yLrwS/cthq";
    }
}
