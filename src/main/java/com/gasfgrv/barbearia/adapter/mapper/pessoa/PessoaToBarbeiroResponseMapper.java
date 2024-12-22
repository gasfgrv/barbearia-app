package com.gasfgrv.barbearia.adapter.mapper.pessoa;

import com.gasfgrv.barbearia.adapter.controller.pessoa.BarbeiroResponse;
import com.gasfgrv.barbearia.domain.entity.Pessoa;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PessoaToBarbeiroResponseMapper implements Mapper<Pessoa, BarbeiroResponse> {

    private final ModelMapper mapper;

    @Override
    public BarbeiroResponse map(Pessoa input) {
        return mapper.map(input, BarbeiroResponse.class);
    }

}
