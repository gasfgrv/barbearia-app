package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.controller.servico.AtualizarServicoForm;
import com.gasfgrv.barbearia.adapter.mapper.servico.AtualizarServicoFormToServicoMapper;
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
@SpringBootTest(classes = {AtualizarServicoFormToServicoMapper.class, ModelMapperConfig.class})
class AtualizarServicoFormToServicoMapperTest {

    @Autowired
    Mapper<AtualizarServicoForm, Servico> mapper;

    @Test
    @DisplayName("Deve converter um objeto AtualizarServicoForm para Servico")
    void deveConverterUmObjetoAtualizarServicoFormParaServico() {
        AtualizarServicoForm form = ServicoMock.montarServicoAtualizacaoForm();
        Servico servico = mapper.map(form);
        assertNotNull(servico);
    }

}