package com.gasfgrv.barbearia.adapter.controller.servico;

import com.gasfgrv.barbearia.adapter.database.servico.ServicoJpaRepository;
import com.gasfgrv.barbearia.config.AbstractContainerIntegrationTestConfig;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.entity.ServicoMock;
import com.gasfgrv.barbearia.domain.port.database.servico.ServicoRepositoryPort;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

class ServicoControllerTest extends AbstractContainerIntegrationTestConfig {

    @Autowired
    WebApplicationContext context;

    @Autowired
    ServicoRepositoryPort repository;

    @Autowired
    ServicoJpaRepository jpa;

    private final Map<String, UUID> idServicos = new HashMap<>();

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.basePath = "/v1/servicos";
        RestAssuredMockMvc.webAppContextSetup(context);
        salvarServicoDeTesteNaBaseDeDados();
    }

    @AfterEach
    void tearDown() {
        jpa.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar um novo serviço quando for um barbeiro")
    @WithMockUser(username = "barbeiro", authorities = "BARBEIRO")
    void deveSalvarUmNovoServicoQuandoForUmBarbeiro() {
        RestAssuredMockMvc
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .body(montarBodyDeCadastroDeServico())
                .when()
                    .post()
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_CREATED)
                    .header(HttpHeaders.LOCATION, notNullValue());
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar salvar um novo serviço quando for um cliente")
    @WithMockUser(username = "cliente", authorities = "CLIENTE")
    void deveLancarErroAoTentarSalvarUmNovoServicoQuandoForUmCliente() {
        RestAssuredMockMvc
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .body(montarBodyDeCadastroDeServico())
                .when()
                    .post()
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Deve listar todos os serviços - barbeiro")
    @WithMockUser(username = "barbeiro", authorities = "BARBEIRO")
    void deveListarTodosOsServicosBarbeiro() {
        RestAssuredMockMvc
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .params(montarParamsParaConsulta(false))
                .when()
                    .get()
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("", hasSize(2));
    }

    @Test
    @DisplayName("Deve listar todos os serviços - cliente")
    @WithMockUser(username = "cliente", authorities = "CLIENTE")
    void deveListarTodosOsServicosCliente() {
        RestAssuredMockMvc
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .params(montarParamsParaConsulta(true))
                .when()
                    .get()
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK)
                    .body("", hasSize(1));
    }

    @Test
    @DisplayName("Deve consultar os dados de um serviço")
    @WithMockUser(username = "barbeiro", authorities = "BARBEIRO")
    void deveConsultarOsDadosDeUmServico() {
        RestAssuredMockMvc
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id_servico", idServicos.get("ativo"))
                .when()
                    .get("/{id_servico}")
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Deve lançar erro ao consultar um serviço que não existe")
    @WithMockUser(username = "cliente", authorities = "CLIENTE")
    void deveLancarErroAoConsultarUmServicoQueNaoExiste() {
        RestAssuredMockMvc
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id_servico", UUID.randomUUID())
                .when()
                    .get("/{id_servico}")
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("Deve reativar um serviço")
    @WithMockUser(username = "barbeiro", authorities = "BARBEIRO")
    void deveReativarUmServico() {
        RestAssuredMockMvc
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id_servico", idServicos.get("inativo"))
                .when()
                    .put("/{id_servico}/reativar")
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Deve lançar erro ao reativar um serviço")
    @WithMockUser(username = "cliente", authorities = "CLIENTE")
    void deveLancarErroAoReativarUmServico() {
        RestAssuredMockMvc
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id_servico", idServicos.get("inativo"))
                .when()
                    .put("/{id_servico}/reativar")
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Deve desativar um serviço")
    @WithMockUser(username = "barbeiro", authorities = "BARBEIRO")
    void deveDesativarUmServico() {
        RestAssuredMockMvc
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id_servico", idServicos.get("ativo"))
                .when()
                    .put("/{id_servico}/desativar")
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Deve lançar erro ao desaativar um serviço")
    @WithMockUser(username = "cliente", authorities = "CLIENTE")
    void deveLancarErroAoDesativarUmServico() {
        RestAssuredMockMvc
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id_servico", idServicos.get("ativo"))
                .when()
                    .put("/{id_servico}/desativar")
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Deve atualizar um  novo serviço quando for um barbeiro")
    @WithMockUser(username = "barbeiro", authorities = "BARBEIRO")
    void deveAtualizarUmServicoQuandoForUmBarbeiro() {
        RestAssuredMockMvc
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .body(montarBodyDeCadastroDeServico())
                    .pathParam("id_servico", idServicos.get("ativo"))
                .when()
                    .put("/{id_servico}")
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar um serviço quando for um cliente")
    @WithMockUser(username = "cliente", authorities = "CLIENTE")
    void deveLancarErroAoTentarAtualizarUmServicoQuandoForUmCliente() {
        RestAssuredMockMvc
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .body(montarBodyDeAtualizacaoDeServico())
                    .pathParam("id_servico", idServicos.get("ativo"))
                .when()
                    .put("/{id_servico}")
                .then()
                    .log().everything()
                    .assertThat()
                    .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    private void salvarServicoDeTesteNaBaseDeDados() {
        Servico servico = ServicoMock.montarServico();
        servico.setId(UUID.randomUUID());
        repository.salvarServico(servico);
        idServicos.put("ativo", servico.getId());

        Servico servicoDesativado = ServicoMock.montarServicoDesativado();
        servicoDesativado.setId(UUID.randomUUID());
        repository.salvarServico(servicoDesativado);
        idServicos.put("inativo", servicoDesativado.getId());
    }

    private Map<String, String> montarBodyDeCadastroDeServico() {
        return Map.ofEntries(
                Map.entry("nome", "Teste - cadastro de serviço"),
                Map.entry("descricao", "Teste de insert"),
                Map.entry("preco", "1.00"),
                Map.entry("duracao", "10")
        );
    }

    private Map<String, String> montarBodyDeAtualizacaoDeServico() {
        return Map.ofEntries(
                Map.entry("nome", "Teste - atualização de serviço"),
                Map.entry("descricao", "Teste de update de serviço"),
                Map.entry("preco", "10.00"),
                Map.entry("duracao", "30")
        );
    }

    private Map<String, Object> montarParamsParaConsulta(boolean apenasAtivos) {
        return Map.ofEntries(
                Map.entry("pagina", 0),
                Map.entry("quantidade", 5),
                Map.entry("apenas_ativos", apenasAtivos)
        );
    }

}