package com.gasfgrv.barbearia.adapter.database.servico;

import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.entity.ServicoMock;
import com.gasfgrv.barbearia.domain.port.database.servico.ServicoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ServicoRepositoryAdapterTest extends AbstractContainerIntegrationTestConfig {

    @Autowired
    ServicoRepositoryPort repository;

    Servico servico;

    @BeforeEach
    void setUp() {
        servico = ServicoMock.montarServico();
        repository.salvarServico(servico);
    }

    @Test
    @DisplayName("Deve salvar um serviço na base de dados")
    void deveSalvarUmServicoNaBaseDeDados() {
        Optional<Servico> servicoSalvo = repository.obterDadosServico(servico.getId());
        assertTrue(servicoSalvo.isPresent());
        assertEquals(servico.getId(), servicoSalvo.get().getId());
        assertEquals(servico.getNome(), servicoSalvo.get().getNome());
        assertEquals(servico.getDescricao(), servicoSalvo.get().getDescricao());
        assertEquals(0, servico.getPreco().compareTo(servicoSalvo.get().getPreco()));
        assertEquals(servico.getDuracao(), servicoSalvo.get().getDuracao());
        assertEquals(servico.isAtivo(), servicoSalvo.get().isAtivo());
    }

    @Test
    @DisplayName("Deve desativar o serviço")
    void deveDesativarServico() {
        repository.desativarServico(servico);
        Optional<Servico> servicoSalvo = repository.obterDadosServico(servico.getId());
        assertTrue(servicoSalvo.isPresent());
        assertFalse(servicoSalvo.get().isAtivo());
    }

    @Test
    @DisplayName("Deve ativar o serviço")
    void deveAtivarServico() {
        repository.reativarServico(servico);
        Optional<Servico> servicoSalvo = repository.obterDadosServico(servico.getId());
        assertTrue(servicoSalvo.isPresent());
        assertTrue(servicoSalvo.get().isAtivo());
    }

    @Test
    @DisplayName("Deve obter dados de um serviço existente")
    void deveObterDadosDeUmServicoExistente() {
        Optional<Servico> servicoSalvo = repository.obterDadosServico(servico.getId());
        assertTrue(servicoSalvo.isPresent());
    }

    @Test
    @DisplayName("Deve retornar um optional vazio quando o serviço não existir")
    void deveRetornarUmOptionalVazioQuandoServicoNaoExistir() {
        Optional<Servico> servicoSalvo = repository.obterDadosServico(UUID.randomUUID());
        assertTrue(servicoSalvo.isEmpty());
    }

    @Test
    @DisplayName("Deve listar todos os serviços sem precisar passar um pageable")
    void deveListarTodosOsServicosSemPrecisarPassarUmPageable() {
        List<Servico> servicos = repository.listarServicos();
        assertFalse(servicos.isEmpty());
    }

    @Test
    @DisplayName("Deve listar todos os serviços ativos passando um pageable")
    void deveListarTodosOsServicosAtivosPassandoUmPageable() {
        Servico servicoDesativado = ServicoMock.montarServicoDesativado();
        repository.salvarServico(servicoDesativado);

        List<Servico> servicos = repository.listarServicos(true, 0, 5);
        assertFalse(servicos.stream().anyMatch(s -> s.getId().equals(servicoDesativado.getId())));
    }

    @Test
    @DisplayName("Deve listar todos os serviços passando um pageable")
    void deveListarTodosOsServicosPassandoUmPageable() {
        Servico servicoDesativado = ServicoMock.montarServicoDesativado();
        repository.salvarServico(servicoDesativado);

        List<Servico> servicos = repository.listarServicos(false, 0, 5);
        assertTrue(servicos.stream().anyMatch(s -> s.getId().equals(servicoDesativado.getId())));
    }

}
