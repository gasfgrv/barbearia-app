package com.gasfgrv.barbearia.domain.port.database.pessoa;

import com.gasfgrv.barbearia.domain.entity.Pessoa;

import java.util.UUID;

public interface PessoaRepositoryPort {

    Pessoa salvarPessa(Pessoa pessoa);

    boolean existeCpfSalvo(String cpf);

    Pessoa buscarPessoa(UUID idPessoa);

    void removerPessoa(UUID idPessoa);
}
