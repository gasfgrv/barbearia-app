package com.gasfgrv.barbearia.adapter.database.perfil;

import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import com.gasfgrv.barbearia.domain.entity.Perfil;
import com.gasfgrv.barbearia.domain.port.database.perfil.PerfilRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PerfilRepositoryAdapterTest extends AbstractContainerIntegrationTestConfig {

    @Autowired
    PerfilRepositoryPort repository;

    @Test
    @DisplayName("Deve obter os dados do perfil")
    void deveObterOsDadosDoPerfil() {
        Perfil cliente = repository.findByNome("cliente");
        Perfil barbeiro = repository.findByNome("barbeiro");

        assertNotNull(cliente);
        assertNotNull(barbeiro);
    }

    @Test
    @DisplayName("Deve retornar nulo quando não existe os dados do perfil")
    void deveRetornarNuloQuandoNaoExisteOsDadosDoPerfil() {
        PerfilSchema schema = PerfilSchemaMock.montarPerfilSchema();
        Perfil perfil = repository.findByNome(schema.getNome());

        assertNull(perfil);
    }

}
