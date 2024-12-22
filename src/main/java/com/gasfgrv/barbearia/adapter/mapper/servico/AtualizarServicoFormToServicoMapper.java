package com.gasfgrv.barbearia.adapter.mapper.servico;

import com.gasfgrv.barbearia.adapter.controller.servico.AtualizarServicoForm;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AtualizarServicoFormToServicoMapper implements Mapper<AtualizarServicoForm, Servico> {

    private final ModelMapper mapper;

    @Override
    public Servico map(AtualizarServicoForm input) {
        return mapper.map(input, Servico.class);
    }

}
