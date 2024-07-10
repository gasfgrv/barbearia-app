package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.database.servico.ServicoSchema;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServicoToServicoSchemaMapper implements Mapper<Servico, ServicoSchema> {

    private final ModelMapper mapper;

    @Override
    public ServicoSchema map(Servico input) {
        return mapper.map(input, ServicoSchema.class);
    }

}
