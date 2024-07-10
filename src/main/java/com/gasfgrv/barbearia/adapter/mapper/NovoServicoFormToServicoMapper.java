package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.controller.servico.ServicoResponse;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NovoServicoFormToServicoMapper implements Mapper<Servico, ServicoResponse> {

    private final ModelMapper mapper;

    @Override
    public ServicoResponse map(Servico input) {
        return mapper.map(input, ServicoResponse.class);
    }

}
