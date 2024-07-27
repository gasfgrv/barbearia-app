package com.gasfgrv.barbearia.adapter.database.servico;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.UUID;

@UtilityClass
class ServicoSchemaMock {

    public ServicoSchema montarServicoSchema() {
        ServicoSchema servico = new ServicoSchema();
        servico.setId(UUID.fromString("9d2b691a-5009-4fc5-ab69-1a50099fc516"));
        servico.setNome("Teste");
        servico.setDescricao("Serviço de teste");
        servico.setPreco(BigDecimal.TEN);
        servico.setDuracao(10);
        servico.setAtivo(true);
        return servico;
    }

}