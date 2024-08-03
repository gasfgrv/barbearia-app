package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.controller.servico.NovoServicoForm;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NovoServicoFormToServicoMapper implements Mapper<NovoServicoForm, Servico> {

    private final ModelMapper mapper;

    @Override
    public Servico map(NovoServicoForm input) {
        return mapper.map(input, Servico.class);
    }

}
