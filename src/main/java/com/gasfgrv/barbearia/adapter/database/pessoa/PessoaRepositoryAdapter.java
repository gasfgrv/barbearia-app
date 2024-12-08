package com.gasfgrv.barbearia.adapter.database.pessoa;

import com.gasfgrv.barbearia.domain.entity.Pessoa;
import com.gasfgrv.barbearia.domain.port.database.pessoa.PessoaRepositoryPort;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PessoaRepositoryAdapter implements PessoaRepositoryPort {

    private final PessoaJpaRepository repository;
    private final Mapper<Pessoa, PessoaSchema> pessoaToPessoaSchemaMapper;
    private final Mapper<PessoaSchema, Pessoa> pessoaSchemaToPessoaMapper;

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "pessoa", key = "#pessoa.id"),
            @CacheEvict(cacheNames = "pessoas", allEntries = true),
            @CacheEvict(cacheNames = "existeCpfSalvo", key = "#pessoa.cpf")
    })
    public Pessoa salvarPessa(Pessoa pessoa) {
        PessoaSchema schema = pessoaToPessoaSchemaMapper.map(pessoa);
        PessoaSchema saved = repository.save(schema);
        return pessoaSchemaToPessoaMapper.map(saved);
    }

    @Override
    @Cacheable(cacheNames = "existeCpfSalvo", key = "#cpf")
    public boolean existeCpfSalvo(String cpf) {
        return repository.existsByCpf(cpf);
    }

}
