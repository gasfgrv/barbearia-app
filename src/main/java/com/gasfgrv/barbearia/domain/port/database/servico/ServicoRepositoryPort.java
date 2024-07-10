package com.gasfgrv.barbearia.domain.port.database.servico;

import com.gasfgrv.barbearia.domain.entity.Servico;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServicoRepositoryPort {

    Servico salvarServico(Servico servico);

    void desativarServico(Servico servico);

    void reativarServico(Servico servico);

    Optional<Servico> obterDadosServico(UUID idServico);

    List<Servico> listarServicos();

    List<Servico> listarServicos(boolean listarApenasAtivos, int pagina, int quantidade);

}
