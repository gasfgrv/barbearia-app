package com.gasfgrv.barbearia.adapter.controller.servico;

import com.gasfgrv.barbearia.application.service.servico.ServicoService;
import com.gasfgrv.barbearia.domain.entity.Servico;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static com.gasfgrv.barbearia.adapter.utils.RequestUtils.capturarQueryParameters;
import static com.gasfgrv.barbearia.adapter.utils.RequestUtils.logRequisicaoRecebida;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/servicos")
public class ServicoController {

    private final ServicoService servicoService;
    private final Mapper<NovoServicoForm, Servico> novoServicoFormToServicoMapper;
    private final Mapper<Servico, ServicoResponse> servicoToServicoResponseMapper;
    private final Mapper<AtualizarServicoForm, Servico> atualizarServicoFormToServicoMapper;

    @Transactional
    @PostMapping
    public ResponseEntity<ServicoResponse> salvarServico(@RequestBody @Valid NovoServicoForm form, HttpServletRequest request) {
        logRequisicaoRecebida(request);
        Servico novoServico = servicoService.criarServico(novoServicoFormToServicoMapper.map(form));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment(novoServico.getId().toString())
                .build()
                .toUri();

        return ResponseEntity.created(location)
                .body(servicoToServicoResponseMapper.map(novoServico));
    }

    @Transactional
    @PutMapping("/{id_servico}/desativar")
    public ResponseEntity<Void> desativarServico(@PathVariable("id_servico") UUID idServico, HttpServletRequest request) {
        logRequisicaoRecebida(request);
        servicoService.desativarServico(idServico);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PutMapping("/{id_servico}/reativar")
    public ResponseEntity<Void> reativarServico(@PathVariable("id_servico") UUID idServico, HttpServletRequest request) {
        logRequisicaoRecebida(request);
        servicoService.reativarServico(idServico);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PutMapping("/{id_servico}")
    public ResponseEntity<ServicoResponse> atualizarServico(@PathVariable("id_servico") UUID idServico,
                                                            @RequestBody @Valid AtualizarServicoForm form,
                                                            HttpServletRequest request) {
        logRequisicaoRecebida(request);
        Servico novoServico = servicoService.atualizarServico(idServico, atualizarServicoFormToServicoMapper.map(form));
        return ResponseEntity.ok(servicoToServicoResponseMapper.map(novoServico));
    }

    @GetMapping("/{id_servico}")
    public ResponseEntity<ServicoResponse> buscarServico(@PathVariable("id_servico") UUID idServico, HttpServletRequest request) {
        logRequisicaoRecebida(request);
        Servico servico = servicoService.obterDadosServico(idServico);
        return ResponseEntity.ok(servicoToServicoResponseMapper.map(servico));
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponse>> listarServicos(@RequestParam(value = "pagina", defaultValue = "0") int pagina,
                                                                @RequestParam(value = "quantidade", defaultValue = "5") int quantidade,
                                                                @RequestParam(value = "apenas_ativos", defaultValue = "true") boolean apenasAtivos,
                                                                HttpServletRequest request) {
        logRequisicaoRecebida(request, capturarQueryParameters(request));
        List<ServicoResponse> paginaServicos = servicoService.listarServicos(apenasAtivos, pagina, quantidade)
                .stream().map(servicoToServicoResponseMapper::map)
                .toList();

        return ResponseEntity.ok(paginaServicos);
    }

}
