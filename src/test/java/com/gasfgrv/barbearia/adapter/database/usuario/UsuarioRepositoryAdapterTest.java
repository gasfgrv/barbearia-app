package com.gasfgrv.barbearia.adapter.database.usuario;

import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.entity.UsuarioMock;
import com.gasfgrv.barbearia.domain.port.database.usuario.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsuarioRepositoryAdapterTest extends AbstractContainerIntegrationTestConfig {

    static final String BCRYPT_PATTERN = "^\\$2[aby]?\\$\\d{2}\\$[./A-Za-z0-9]{53}$";

    @Autowired
    UsuarioRepositoryPort repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = UsuarioMock.montarUsuario();
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        repository.salvarUsuario(usuario);
    }

    @Test
    @DisplayName("Deve inserir os dados do usuário na base de dados")
    void deveInserirOsDadosDoUsuarioNaBaseDeDados() {
        Usuario atual = repository.findByLogin(usuario.getLogin());

        assertEquals(usuario.getLogin(), atual.getLogin());
        assertTrue(atual.getSenha().matches(BCRYPT_PATTERN));
    }

    @Test
    @DisplayName("Deve obter os dados do usuário")
    void deveObterOsDadosDoUsuario() {
        Usuario atual = repository.findByLogin(usuario.getLogin());

        assertNotNull(atual);
        assertEquals(usuario.getLogin(), atual.getLogin());
        assertTrue(atual.getSenha().matches(BCRYPT_PATTERN));
    }

    @Test
    @DisplayName("Deve retornar nulo quando não houver dados do usuário")
    void deveRetornarNuloQuandoNaoHouverDadosDoUsuario() {
        Usuario atual = repository.findByLogin("mockUser");

        assertNull(atual);
    }

}