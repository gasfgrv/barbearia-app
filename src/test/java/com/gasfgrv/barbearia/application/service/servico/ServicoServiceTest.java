package com.gasfgrv.barbearia.application.service.servico;

import com.gasfgrv.barbearia.application.exception.servico.SemDadosParaAlterarException;
import com.gasfgrv.barbearia.application.exception.servico.ServicoAtivoExeception;
import com.gasfgrv.barbearia.application.exception.servico.ServicoDesativadoExeception;
import com.gasfgrv.barbearia.application.exception.servico.ServicoExistenteExeception;
import com.gasfgrv.barbearia.application.exception.servico.ServicoNaoEncontradoException;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.entity.ServicoMock;
import com.gasfgrv.barbearia.domain.port.database.servico.ServicoRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class ServicoServiceTest {

    @Mock
    ServicoRepositoryPort repository;

    @InjectMocks
    ServicoService service;

    @Test
    @DisplayName("Deve criar um novo serviço")
    void deveCriarUmNovoServico() {
        Servico servico = ServicoMock.montarNovoServico();
        ArgumentCaptor<Servico> captor = ArgumentCaptor.forClass(Servico.class);

        when(repository.listarServicos()).thenReturn(Collections.emptyList());
        when(repository.salvarServico(captor.capture())).thenReturn(servico);

        Servico novoServico = service.criarServico(servico);

        assertNotNull(novoServico);
        assertEquals(servico, novoServico);

        assertEquals(servico.getId(), captor.getValue().getId());
        assertTrue(servico.getId().toString().matches("^[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"));

        assertEquals(servico.isAtivo(), captor.getValue().isAtivo());
        assertTrue(servico.isAtivo());

        verify(repository, times(1)).listarServicos();
        verify(repository, times(1)).salvarServico(captor.getValue());
    }

    @Test
    @DisplayName("Deve lançar ServicoExistenteExeception ao tentar criar um novo serviço")
    void deveLancarServicoExistenteExeceptionAoTentarCriarUmNovoServico() {
        Servico servico = ServicoMock.montarNovoServico();

        when(repository.listarServicos()).thenReturn(Collections.singletonList(servico));

        assertThrows(ServicoExistenteExeception.class, () -> service.criarServico(servico));

        verify(repository, times(1)).listarServicos();
        verify(repository, never()).salvarServico(any(Servico.class));
    }

    @Test
    @DisplayName("Deve desativar o serviço")
    void deveDesativarOServico() {
        Servico servico = ServicoMock.montarServico();

        when(repository.obterDadosServico(servico.getId())).thenReturn(Optional.of(servico));
        doNothing().when(repository).desativarServico(any(Servico.class));

        service.desativarServico(servico.getId());

        verify(repository, times(1)).obterDadosServico(any(UUID.class));
        verify(repository, times(1)).desativarServico(any(Servico.class));
    }

    @Test
    @DisplayName("Deve lançar ServicoDesativadoExeception ao tentar desativar o serviço")
    void deveLancarServicoDesativadoExeceptionAoTentarDesativarOServico() {
        Servico servico = ServicoMock.montarServicoDesativado();
        UUID servicoId = servico.getId();

        when(repository.obterDadosServico(servicoId)).thenReturn(Optional.of(servico));

        assertThrows(ServicoDesativadoExeception.class, () -> service.desativarServico(servicoId));

        verify(repository, times(1)).obterDadosServico(any(UUID.class));
        verify(repository, never()).desativarServico(any(Servico.class));
    }

    @Test
    @DisplayName("Deve reativar o serviço")
    void deveReativarServico() {
        Servico servico = ServicoMock.montarServicoDesativado();

        when(repository.obterDadosServico(servico.getId())).thenReturn(Optional.of(servico));
        doNothing().when(repository).reativarServico(any(Servico.class));

        service.reativarServico(servico.getId());

        verify(repository, times(1)).obterDadosServico(any(UUID.class));
        verify(repository, times(1)).reativarServico(any(Servico.class));
    }

    @Test
    @DisplayName("Deve lançar ServicoServicoAtivoExeception ao tentar reativar o serviço")
    void deveLancarServicoServicoAtivoExeceptionAoTentarReativarServico() {
        Servico servico = ServicoMock.montarServico();
        UUID servicoId = servico.getId();

        when(repository.obterDadosServico(servicoId)).thenReturn(Optional.of(servico));

        assertThrows(ServicoAtivoExeception.class, () -> service.reativarServico(servicoId));

        verify(repository, times(1)).obterDadosServico(any(UUID.class));
        verify(repository, never()).reativarServico(any(Servico.class));
    }

    @Test
    @DisplayName("Deve atualizar serviço")
    void deveAtualizarServico() {
        ArgumentCaptor<Servico> captor = ArgumentCaptor.forClass(Servico.class);

        Servico servicoAtualizacao = ServicoMock.montarServicoAtualizacao();

        Servico servico = ServicoMock.montarServico();
        UUID servicoId = servico.getId();
        String nomeAntigo = servico.getNome();
        String descricaoAntiga = servico.getDescricao();

        when(repository.obterDadosServico(servicoId)).thenReturn(Optional.of(servico));
        when(repository.salvarServico(captor.capture())).thenReturn(servico);

        Servico servicoAtualizado = service.atualizarServico(servicoId, servicoAtualizacao);
        Servico captorValue = captor.getValue();

        assertNotNull(servicoAtualizado);

        assertNotNull(servicoAtualizado.getNome());
        assertNotEquals(nomeAntigo, servicoAtualizado.getNome());
        assertEquals(captorValue.getNome(), servicoAtualizado.getNome());

        assertNotNull(servicoAtualizado.getDescricao());
        assertNotEquals(descricaoAntiga, servicoAtualizado.getDescricao());
        assertEquals(captorValue.getDescricao(), servicoAtualizado.getDescricao());

        assertNotNull(servicoAtualizado.getPreco());
        assertEquals(1, servicoAtualizado.getPreco().compareTo(BigDecimal.TEN));
        assertEquals(captorValue.getPreco(), servicoAtualizado.getPreco());

        assertEquals(1, Integer.compare(servicoAtualizado.getDuracao(), 10));
        assertEquals(captorValue.getDuracao(), servicoAtualizado.getDuracao());

        verify(repository, times(1)).obterDadosServico(any(UUID.class));
        verify(repository, times(1)).salvarServico(captorValue);
    }

    @Test
    @DisplayName("Deve alterar o serviço quando duração == 0")
    void deveAlterarServicoQuandoDuracaoForIgualZero() {
        Servico servicoAtualizacao = ServicoMock.montarServicoAtualizacao();
        servicoAtualizacao.setDuracao(0);

        Servico servico = ServicoMock.montarServico();
        UUID servicoId = servico.getId();

        when(repository.obterDadosServico(servicoId)).thenReturn(Optional.of(servico));

        assertDoesNotThrow(() -> service.atualizarServico(servicoId, servicoAtualizacao));

        verify(repository, times(1)).obterDadosServico(any(UUID.class));
        verify(repository, times(1)).salvarServico(any(Servico.class));
    }

    @Test
    @DisplayName("Deve alterar o serviço quando preco == null")
    void deveAlterarServicoQuandoPrecoIgualNulo() {
        Servico servicoAtualizacao = ServicoMock.montarServicoAtualizacao();
        servicoAtualizacao.setPreco(null);

        Servico servico = ServicoMock.montarServico();
        UUID servicoId = servico.getId();

        when(repository.obterDadosServico(servicoId)).thenReturn(Optional.of(servico));

        assertDoesNotThrow(() -> service.atualizarServico(servicoId, servicoAtualizacao));

        verify(repository, times(1)).obterDadosServico(any(UUID.class));
        verify(repository, times(1)).salvarServico(any(Servico.class));
    }

    @Test
    @DisplayName("Deve alterar o serviço quando nome ou descrição forem vazias")
    void deveAlterarServicoQuandoNomeOuDescricaoEmpty() {
        Servico servicoAtualizacao = ServicoMock.montarServicoAtualizacao();
        servicoAtualizacao.setNome("");
        servicoAtualizacao.setDescricao(null);

        Servico servico = ServicoMock.montarServico();
        UUID servicoId = servico.getId();

        when(repository.obterDadosServico(servicoId)).thenReturn(Optional.of(servico));

        assertDoesNotThrow(() -> service.atualizarServico(servicoId, servicoAtualizacao));

        verify(repository, times(1)).obterDadosServico(any(UUID.class));
        verify(repository, times(1)).salvarServico(any(Servico.class));
    }

    @Test
    @DisplayName("Deve lançar SemDadosParaAlterarException quando não houver alteracao")
    void deveLancarSemDadosParaAlterarExceptionQuandoNaoHouverAlteracao() {
        Servico servicoAtualizacao = ServicoMock.montarServicoAtualizacaoVazio();

        Servico servico = ServicoMock.montarServico();
        UUID servicoId = servico.getId();

        when(repository.obterDadosServico(servicoId)).thenReturn(Optional.of(servico));

        assertThrows(SemDadosParaAlterarException.class, () -> service.atualizarServico(servicoId, servicoAtualizacao));

        verify(repository, times(1)).obterDadosServico(any(UUID.class));
        verify(repository, never()).salvarServico(any(Servico.class));
    }

    @Test
    @DisplayName("Deve obter dados do serviço")
    void deveObterDadosDoServico() {
        Servico servico = ServicoMock.montarServico();

        when(repository.obterDadosServico(servico.getId())).thenReturn(Optional.of(servico));

        Servico servicoObtido = service.obterDadosServico(servico.getId());

        assertNotNull(servicoObtido);
        assertInstanceOf(Servico.class, servicoObtido);

        verify(repository, times(1)).obterDadosServico(any(UUID.class));
    }

    @Test
    @DisplayName("Deve lançar ServicoNaoEncontradoException ao tentar obter dados do serviço")
    void deveLancarServicoNaoEncontradoExceptionAoTentarObterDadosDoServico() {
        when(repository.obterDadosServico(any(UUID.class))).thenReturn(Optional.empty());

        UUID idServico = UUID.randomUUID();
        assertThrows(ServicoNaoEncontradoException.class, () -> service.obterDadosServico(idServico));

        verify(repository, times(1)).obterDadosServico(any(UUID.class));
    }

    @Test
    @DisplayName("Deve listar todos os serviços")
    void deveListarTodosOsServicos(CapturedOutput output) {
        Servico servicoAtivo = ServicoMock.montarServico();
        Servico servicoInativo = ServicoMock.montarServicoDesativado();

        when(repository.listarServicos(eq(false), anyInt(), anyInt()))
                .thenReturn(List.of(servicoAtivo, servicoInativo));

        List<Servico> list = service.listarServicos(false, 0, 2);

        assertEquals(2, list.size());
        assertTrue(output.getOut().contains("Listando todos serviços"));

        verify(repository, times(1)).listarServicos(anyBoolean(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Deve listar apenas serviços ativos")
    void deveListarApenasOsServicosAtivos(CapturedOutput output) {
        Servico servicoAtivo = ServicoMock.montarServico();

        when(repository.listarServicos(eq(true), anyInt(), anyInt()))
                .thenReturn(List.of(servicoAtivo));

        List<Servico> list = service.listarServicos(true, 0, 2);

        assertEquals(1, list.size());
        assertTrue(output.getOut().contains("Listando serviços ativos"));

        verify(repository, times(1)).listarServicos(anyBoolean(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Deve retonar uma lista vazia se não tiver serviços")
    void deveRetornarUmaListaVaziarQaundoNaoTiverServicos() {

        when(repository.listarServicos(anyBoolean(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        List<Servico> todos = service.listarServicos(false, 0, 1);
        List<Servico> ativos = service.listarServicos(true, 0, 1);

        boolean todasAsListasSaoVazias = todos.isEmpty() && ativos.isEmpty();
        assertTrue(todasAsListasSaoVazias);

        verify(repository, times(1)).listarServicos(eq(true), anyInt(), anyInt());
        verify(repository, times(1)).listarServicos(eq(false), anyInt(), anyInt());
    }

}
