package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.controller.servico.NovoServicoForm;
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
@SpringBootTest(classes = {NovoServicoFormToServicoMapper.class, ModelMapperConfig.class})
class NovoServicoFormToServicoMapperTest {

    @Autowired
    Mapper<NovoServicoForm, Servico> mapper;

    @Test
    @DisplayName("Deve converter um objeto NovoServicoForm para Servico")
    void deveConverterUmObjetoNovoServicoFormParaServico() {
        NovoServicoForm form = ServicoMock.montarNovoServicoForm();
        Servico servico = mapper.map(form);
        assertNotNull(servico);
    }

}