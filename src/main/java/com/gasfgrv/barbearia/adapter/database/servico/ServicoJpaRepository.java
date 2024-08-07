package com.gasfgrv.barbearia.adapter.database.servico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServicoJpaRepository extends JpaRepository<ServicoSchema, UUID> {

    List<ServicoSchema> findByAtivoTrue();

    Page<ServicoSchema> findByAtivoTrue(Pageable pageable);

}
