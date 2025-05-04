package com.gasfgrv.barbearia.adapter.mapper.pessoa;

import com.gasfgrv.barbearia.adapter.controller.pessoa.PessoaResponse;
import com.gasfgrv.barbearia.domain.entity.Pessoa;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PessoaToPessoaResponseMapper implements Mapper<Pessoa, PessoaResponse> {

    private final ModelMapper modelMapper;

    @Override
    public PessoaResponse map(Pessoa input) {
        return modelMapper.map(input, PessoaResponse.class);
    }

    @PostConstruct
    private void configurarMapper() {
        modelMapper.typeMap(Pessoa.class, PessoaResponse.class)
                .addMappings(mapper -> mapper
                        .map(pessoa -> pessoa.getUsuario().getLogin(),
                                PessoaResponse::setLogin));
    }

}
