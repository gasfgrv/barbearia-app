package com.gasfgrv.barbearia.application.service.servico;

import com.gasfgrv.barbearia.application.exception.servico.SemDadosParaAlterarException;
import com.gasfgrv.barbearia.application.exception.servico.ServicoAtivoExeception;
import com.gasfgrv.barbearia.application.exception.servico.ServicoDesativadoExeception;
import com.gasfgrv.barbearia.application.exception.servico.ServicoExistenteExeception;
import com.gasfgrv.barbearia.application.exception.servico.ServicoNaoEncontradoException;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.port.database.servico.ServicoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepositoryPort repository;

    public Servico criarServico(Servico servico) {
        Set<String> servicos = repository.listarServicos().stream()
                .map(Servico::getNome)
                .collect(Collectors.toSet());

        if (!servicos.add(servico.getNome()))
            throw new ServicoExistenteExeception();


        servico.setAtivo(true);
        servico.setId(UUID.randomUUID());
        return repository.salvarServico(servico);
    }

    public void desativarServico(UUID idServico) {
        Servico servico = getServico(idServico);

        if (!servico.isAtivo()) {
            throw new ServicoDesativadoExeception();
        }

        repository.desativarServico(servico);
    }

    public void reativarServico(UUID idServico) {
        Servico servico = getServico(idServico);

        if (servico.isAtivo())
            throw new ServicoAtivoExeception();

        repository.reativarServico(servico);
    }

    public Servico atualizarServico(UUID idServico, Servico servicoAtualizado) {
        Servico servico = getServico(idServico);

        boolean naoHouveAlteracao = servicoAtualizado.getDuracao() == 0
                && servicoAtualizado.getPreco() == null
                && StringUtils.isAllEmpty(servicoAtualizado.getNome(), servicoAtualizado.getDescricao());

        if (naoHouveAlteracao)
            throw new SemDadosParaAlterarException();


        servico.setNome(servicoAtualizado.getNome());
        servico.setDescricao(servicoAtualizado.getDescricao());
        servico.setPreco(servicoAtualizado.getPreco());
        servico.setDuracao(servicoAtualizado.getDuracao());

        return repository.salvarServico(servico);
    }

    public Servico obterDadosServico(UUID idServico) {
        return getServico(idServico);
    }

    public List<Servico> listarServicos(boolean listarApenasAtivos, int pagina, int quantidade) {
        return repository.listarServicos(listarApenasAtivos, pagina, quantidade);
    }

    private Servico getServico(UUID idServico) {
        return repository.obterDadosServico(idServico)
                .orElseThrow(ServicoNaoEncontradoException::new);
    }

}
