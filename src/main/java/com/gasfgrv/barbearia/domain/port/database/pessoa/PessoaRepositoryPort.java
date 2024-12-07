package com.gasfgrv.barbearia.domain.port.database.pessoa;

import com.gasfgrv.barbearia.domain.entity.Pessoa;

public interface PessoaRepositoryPort {

    Pessoa salvarPessa(Pessoa pessoa);

    boolean existeCpfSalvo(String cpf);
}
