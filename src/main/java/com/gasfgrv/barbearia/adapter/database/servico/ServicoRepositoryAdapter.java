package com.gasfgrv.barbearia.adapter.database.servico;

import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.port.database.servico.ServicoRepositoryPort;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
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

@Repository
@RequiredArgsConstructor
public class ServicoRepositoryAdapter implements ServicoRepositoryPort {

    private final ServicoJpaRepository repository;
    private final Mapper<Servico, ServicoSchema> servicoToServicoSchemaMapper;
    private final Mapper<ServicoSchema, Servico> servicoSchemaToServicoMapper;

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "servico", key = "#servico.id"),
            @CacheEvict(cacheNames = "servicos", allEntries = true)
    })
    public Servico salvarServico(Servico servico) {
        ServicoSchema entity = servicoToServicoSchemaMapper.map(servico);
        ServicoSchema save = repository.save(entity);
        return servicoSchemaToServicoMapper.map(save);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "servico", key = "#servico.id"),
            @CacheEvict(cacheNames = "servicos", allEntries = true)
    })
    public void desativarServico(Servico servico) {
        alterarStatusDoServico(servico, false);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "servico", key = "#servico.id"),
            @CacheEvict(cacheNames = "servicos", allEntries = true)
    })
    public void reativarServico(Servico servico) {
        alterarStatusDoServico(servico, true);
    }

    @Override
    @Cacheable(cacheNames = "servico", key = "#idServico")
    public Optional<Servico> obterDadosServico(UUID idServico) {
        return repository.findById(idServico)
                .map(servicoSchemaToServicoMapper::map);
    }

    @Override
    @Cacheable(cacheNames = "servicos")
    public List<Servico> listarServicos() {
        return repository.findAll().stream()
                .map(servicoSchemaToServicoMapper::map)
                .toList();
    }

    @Override
    public List<Servico> listarServicos(boolean listarApenasAtivos, int pagina, int quantidade) {
        Pageable paginacao = PageRequest.of(pagina, quantidade, Sort.by("nome"));

        Page<ServicoSchema> servicos = listarApenasAtivos
                ? repository.findByAtivoTrue(paginacao)
                : repository.findAll(paginacao);

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
