package com.gasfgrv.barbearia.application.service.perfil;

import com.gasfgrv.barbearia.domain.entity.Perfil;
import com.gasfgrv.barbearia.domain.entity.PerfilMock;
import com.gasfgrv.barbearia.domain.port.database.perfil.PerfilRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PerfilServiceTest {

    @Mock
    private PerfilRepositoryPort repository;

    @InjectMocks
    private PerfilService service;

    private Perfil perfil;

    @BeforeEach
    void setPerfil() {
        perfil = PerfilMock.montarPerfil();
    }

    @Test
    @DisplayName("Deve retornar um perfil da base de dados")
    void deveRetornarUmPerfilDaBaseDeDados() {
        when(repository.findByNome(anyString())).thenReturn(perfil);

        Perfil teste = service.obterPerfil("Teste");

        assertEquals("TESTE", teste.getNome());
        assertEquals(1, teste.getId());
    }

}