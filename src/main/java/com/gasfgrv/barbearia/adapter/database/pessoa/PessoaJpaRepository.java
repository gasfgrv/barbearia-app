package com.gasfgrv.barbearia.adapter.database.pessoa;

import com.gasfgrv.barbearia.adapter.database.servico.ServicoSchema;
import com.gasfgrv.barbearia.domain.entity.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PessoaJpaRepository extends JpaRepository<PessoaSchema, UUID> {

    List<ServicoSchema> findByAtivoTrue();

    Page<ServicoSchema> findByAtivoTrue(Pageable pageable);

    boolean existsByCpf(String cpf);
}
