package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.controller.servico.ServicoResponse;
import com.gasfgrv.barbearia.config.mapper.ModelMapperConfig;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.entity.ServicoMock;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ServicoToServicoResponseMapper.class, ModelMapperConfig.class})
class ServicoToServicoResponseMapperTest {

    @Autowired
    Mapper<Servico, ServicoResponse> mapper;

    @Test
    @DisplayName("Deve converter um objeto Servico para ServicoResponse")
    void deveConverterUmObjetoServicoParaServicoResponse() {
        Servico servico = ServicoMock.montarServico();
        ServicoResponse response = mapper.map(servico);
        assertNotNull(response);
    }

}