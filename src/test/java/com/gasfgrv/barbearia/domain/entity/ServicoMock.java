package com.gasfgrv.barbearia.domain.entity;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@UtilityClass
public class ServicoMock {

    public Servico montarServico() {
        Servico servico = new Servico();
        servico.setId(UUID.fromString("9d2b691a-5009-4fc5-ab69-1a50099fc516"));
        servico.setNome("Teste");
        servico.setDescricao("Serviço de teste");
        servico.setPreco(BigDecimal.TEN);
        servico.setDuracao(10);
        servico.setAtivo(true);
        return servico;
    }

    public Servico montarNovoServico() {
        Servico servico = new Servico();
        servico.setNome("Teste");
        servico.setDescricao("Serviço de teste");
        servico.setPreco(BigDecimal.TEN);
        servico.setDuracao(10);
        return servico;
    }

    public static Servico montarServicoDesativado() {
        Servico servico = new Servico();
        servico.setId(UUID.fromString("9d2b691a-5009-4fc5-ab69-1a50099fc516"));
        servico.setNome("Teste");
        servico.setDescricao("Serviço de teste");
        servico.setPreco(BigDecimal.TEN);
        servico.setDuracao(10);
        servico.setAtivo(false);
        return servico;
    }

    public static Servico montarServicoAtualizacao() {
        Random random = new Random();
        Servico servico = new Servico();
        servico.setNome("Teste atualização");
        servico.setDescricao("Serviço de teste de atualização");
        servico.setPreco(BigDecimal.valueOf(random.nextLong(11, 99)));
        servico.setDuracao(random.nextInt(11, 99));
        return servico;
    }

    public static Servico montarServicoAtualizacaoVazio() {
        return new Servico();
    }
}