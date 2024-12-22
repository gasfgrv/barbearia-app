package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.database.servico.ServicoSchema;
import com.gasfgrv.barbearia.adapter.database.servico.ServicoSchemaMock;
import com.gasfgrv.barbearia.adapter.mapper.servico.ServicoSchemaToServicoMapper;
import com.gasfgrv.barbearia.config.mapper.ModelMapperConfig;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {ServicoSchemaToServicoMapper.class, ModelMapperConfig.class})
class ServicoSchemaToServicoMapperTest {

    @Autowired
    Mapper<ServicoSchema, Servico> mapper;

    @Test
    @DisplayName("Deve converter um objeto ServicoSchema para Servico")
    void deveConverterUmObjetoServicoSchemaParaServico() {
        ServicoSchema servicoSchema = ServicoSchemaMock.montarServicoSchema();
        Servico servico = mapper.map(servicoSchema);
        assertNotNull(servico);
    }

}