package com.gasfgrv.barbearia.adapter.database.usuario;

import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.entity.UsuarioMock;
import com.gasfgrv.barbearia.domain.port.database.usuario.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositoryAdapterTest extends AbstractContainerIntegrationTestConfig {

    @Autowired
    UsuarioRepositoryPort repository;

    Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = UsuarioMock.montarUsuario();
    }

    @Test
    @Order(1)
    @DisplayName("Deve inserir os dados do usuário na base de dados")
    void deveInserirOsDadosDoUsuarioNaBaseDeDados() {
        repository.salvarUsuario(usuario);
        Usuario atual = repository.findByLogin(usuario.getLogin());

        assertEquals(usuario.getLogin(), atual.getLogin());
        assertEquals(usuario.getSenha(), atual.getSenha());
    }

    @Test
    @Order(2)
    @DisplayName("Deve obter os dados do usuário")
    void deveObterOsDadosDoUsuario() {
        Usuario atual = repository.findByLogin(usuario.getLogin());

        assertNotNull(atual);
        assertEquals(usuario.getLogin(), atual.getLogin());
        assertEquals(usuario.getSenha(), atual.getSenha());
    }

    @Test
    @Order(3)
    @DisplayName("Deve retornar nulo quando não houver dados do usuário")
    void deveRetornarNuloQuandoNaoHouverDadosDoUsuario() {
        Usuario atual = repository.findByLogin("mockUser");

        assertNull(atual);
    }

}