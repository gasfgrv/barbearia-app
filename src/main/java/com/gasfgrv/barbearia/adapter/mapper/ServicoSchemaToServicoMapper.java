package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.database.servico.ServicoSchema;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServicoSchemaToServicoMapper implements Mapper<ServicoSchema, Servico> {

    private final ModelMapper mapper;

    @Override
    public Servico map(ServicoSchema input) {
        return mapper.map(input, Servico.class);
    }

}
