package com.gasfgrv.barbearia.application.service.servico;

import com.gasfgrv.barbearia.application.exception.servico.SemDadosParaAlterarException;
import com.gasfgrv.barbearia.application.exception.servico.ServicoAtivoExeception;
import com.gasfgrv.barbearia.application.exception.servico.ServicoDesativadoExeception;
import com.gasfgrv.barbearia.application.exception.servico.ServicoExistenteExeception;
import com.gasfgrv.barbearia.application.exception.servico.ServicoNaoEncontradoException;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.port.database.servico.ServicoRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicoService {

    private final ServicoRepositoryPort repository;

    public Servico criarServico(Servico servico) {
        Set<String> servicos = repository.listarServicos().stream()
                .map(Servico::getNome)
                .collect(Collectors.toSet());

        if (!servicos.add(servico.getNome())) {
            log.warn("Serviço já exite na base de dados");
            throw new ServicoExistenteExeception();
        }

        servico.setAtivo(true);
        servico.setId(UUID.randomUUID());

        log.info("Salvando os dados do serviço");
        return repository.salvarServico(servico);
    }

    public void desativarServico(UUID idServico) {
        Servico servico = getServico(idServico);

        if (!servico.isAtivo()) {
            log.warn("Serviço já se encontra desativado");
            throw new ServicoDesativadoExeception();
        }

        log.info("Desativando serviço com o id: {}", idServico);
        repository.desativarServico(servico);
    }

    public void reativarServico(UUID idServico) {
        Servico servico = getServico(idServico);

        if (servico.isAtivo()) {
            log.warn("Serviço já se encontra ativado");
            throw new ServicoAtivoExeception();
        }

        log.info("Ativando serviço com o id: {}", idServico);
        repository.reativarServico(servico);
    }

    public Servico atualizarServico(UUID idServico, Servico servicoAtualizado) {
        Servico servico = getServico(idServico);

        log.info("Verificando se houve alteração");
        boolean naoHouveAlteracao = servicoAtualizado.getDuracao() == 0
                && servicoAtualizado.getPreco() == null
                && StringUtils.isAllEmpty(servicoAtualizado.getNome(), servicoAtualizado.getDescricao());

        if (naoHouveAlteracao) {
            log.error("Não houve alteração nos dados, cancelando atualização");
            throw new SemDadosParaAlterarException();
        }

        log.info("Atualizando os dados do serviço: {}", idServico);
        servico.setNome(servicoAtualizado.getNome());
        servico.setDescricao(servicoAtualizado.getDescricao());
        servico.setPreco(servicoAtualizado.getPreco());
        servico.setDuracao(servicoAtualizado.getDuracao());

        log.info("Salvando dados atualizados do serviço");
        return repository.salvarServico(servico);
    }

    public Servico obterDadosServico(UUID idServico) {
        log.info("Obtendo os dados do serviço: {}", idServico);
        return getServico(idServico);
    }

    public List<Servico> listarServicos(boolean listarApenasAtivos, int pagina, int quantidade) {
        String logMessage = listarApenasAtivos ? "Listando serviços ativos" : "Listando todos serviços";
        log.info("{}: (Página={} Quantidade={})", logMessage, pagina, quantidade);
        return repository.listarServicos(listarApenasAtivos, pagina, quantidade);
    }

    private Servico getServico(UUID idServico) {
        return repository.obterDadosServico(idServico)
                .orElseThrow(ServicoNaoEncontradoException::new);
    }

}
