package com.gasfgrv.barbearia.adapter.database.servico;

import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.port.database.servico.ServicoRepositoryPort;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ServicoRepositoryAdapter implements ServicoRepositoryPort {

    private final ServicoJpaRepository repository;
    private final Mapper<Servico, ServicoSchema> servicoToServicoSchemaMapper;
    private final Mapper<ServicoSchema, Servico> servicoSchemaToServicoMapper;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "servico", key = "#servico.id"),
            @CacheEvict(value = "servicos", allEntries = true),
            @CacheEvict(value = "servicos-page", allEntries = true)
    })
    public Servico salvarServico(Servico servico) {
        log.info("Salvando dados do serviço {}", servico.getId());
        ServicoSchema entity = servicoToServicoSchemaMapper.map(servico);
        ServicoSchema save = repository.save(entity);
        return servicoSchemaToServicoMapper.map(save);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "servico", key = "#servico.id"),
            @CacheEvict(value = "servicos", allEntries = true),
            @CacheEvict(value = "servicos-page", allEntries = true)
    })
    public void desativarServico(Servico servico) {
        log.info("Desativando serviço {}", servico.getId());
        alterarStatusDoServico(servico, false);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "servico", key = "#servico.id"),
            @CacheEvict(value = "servicos", allEntries = true),
            @CacheEvict(value = "servicos-page", allEntries = true)
    })
    public void reativarServico(Servico servico) {
        log.info("Reativando serviço {}", servico.getId());
        alterarStatusDoServico(servico, true);
    }

    @Override
    @Cacheable(value = "servico", key = "#idServico")
    public Optional<Servico> obterDadosServico(UUID idServico) {
        log.info("Adicionando os dados do serviço {} no cache", idServico);
        return repository.findById(idServico)
                .map(servicoSchemaToServicoMapper::map);
    }

    @Override
    public List<Servico> listarServicos() {
        return repository.findAll().stream()
                .map(servicoSchemaToServicoMapper::map)
                .toList();
    }

    @Override
    @Cacheable(value = "servicos", key = "#listarApenasAtivos + '-' + #pagina  + '-' + #quantidade")
    public List<Servico> listarServicos(boolean listarApenasAtivos, int pagina, int quantidade) {
        Pageable paginacao = PageRequest.of(pagina, quantidade, Sort.by("nome"));

        Page<ServicoSchema> servicos;
        String mensagemDeLog;

        if (listarApenasAtivos) {
            servicos = repository.findByAtivoTrue(paginacao);
            mensagemDeLog = "Listando {} serviços ativos na página {}";
        } else {
            servicos = repository.findAll(paginacao);
            mensagemDeLog = "Listando {} serviços na página {}";
        }

        log.info(mensagemDeLog, quantidade, (pagina + 1));
        return servicos.getContent().stream()
                .map(servicoSchemaToServicoMapper::map).
                toList();
    }

    private void alterarStatusDoServico(Servico servico, boolean ativo) {
        ServicoSchema servicoAlterado = servicoToServicoSchemaMapper.map(servico);
        servicoAlterado.setAtivo(ativo);
        repository.save(servicoAlterado);
    }

}
