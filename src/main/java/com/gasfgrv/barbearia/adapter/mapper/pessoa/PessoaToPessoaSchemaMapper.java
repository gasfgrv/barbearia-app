package com.gasfgrv.barbearia.adapter.mapper.pessoa;

import com.gasfgrv.barbearia.adapter.database.pessoa.PessoaSchema;
import com.gasfgrv.barbearia.domain.entity.Pessoa;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PessoaToPessoaSchemaMapper implements Mapper<Pessoa, PessoaSchema> {

    private final ModelMapper mapper;

    @Override
    public PessoaSchema map(Pessoa input) {
        return mapper.map(input, PessoaSchema.class);
    }

}
