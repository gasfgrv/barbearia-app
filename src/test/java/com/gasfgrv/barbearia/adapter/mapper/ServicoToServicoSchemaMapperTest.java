package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.database.servico.ServicoSchema;
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
@SpringBootTest(classes = {ServicoToServicoSchemaMapper.class, ModelMapperConfig.class})
class ServicoToServicoSchemaMapperTest {

    @Autowired
    Mapper<Servico, ServicoSchema> mapper;

    @Test
    @DisplayName("Deve converter um objeto Servico para ServicoSchema")
    void deveConverterUmObjetoServicoParaServicoSchema() {
        Servico servico = ServicoMock.montarServico();
        ServicoSchema servicoSchema = mapper.map(servico);
        assertNotNull(servicoSchema);
    }

}