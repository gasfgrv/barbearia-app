package com.gasfgrv.barbearia.adapter.database.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PessoaJpaRepository extends JpaRepository<PessoaSchema, UUID> {

    boolean existsByCpf(String cpf);

    @Query("select p from PessoaSchema p join fetch p.usuario u where p.id=:id and p.ativo=true")
    Optional<PessoaSchema> buscarPessoaPorId(@Param("id") UUID id);

}
