package com.gasfgrv.barbearia.adapter.mapper.pessoa;

import com.gasfgrv.barbearia.adapter.controller.pessoa.AtualizarDadosPessoaForm;
import com.gasfgrv.barbearia.domain.entity.Pessoa;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AtualizarDadosPessoaFormToPessoaMapper implements Mapper<AtualizarDadosPessoaForm, Pessoa> {

    private final ModelMapper mapper;

    @Override
    public Pessoa map(AtualizarDadosPessoaForm input) {
        return mapper.map(input, Pessoa.class);
    }

}
