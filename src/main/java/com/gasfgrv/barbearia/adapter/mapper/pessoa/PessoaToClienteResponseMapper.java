package com.gasfgrv.barbearia.adapter.mapper.pessoa;

import com.gasfgrv.barbearia.adapter.controller.pessoa.ClienteResponse;
import com.gasfgrv.barbearia.domain.entity.Pessoa;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PessoaToClienteResponseMapper implements Mapper<Pessoa, ClienteResponse> {

    private final ModelMapper mapper;

    @Override
    public ClienteResponse map(Pessoa input) {
        return mapper.map(input, ClienteResponse.class);
    }

}
