package com.gasfgrv.barbearia.adapter.controller.pessoa;

import com.gasfgrv.barbearia.application.service.pessoa.PessoaService;
import com.gasfgrv.barbearia.domain.entity.Arquivo;
import com.gasfgrv.barbearia.domain.entity.Pessoa;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static com.gasfgrv.barbearia.adapter.utils.RequestUtils.logRequisicaoRecebida;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;
    private final Mapper<CadastroClienteForm, Pessoa> cadastroClienteFormToPessoaMapper;
    private final Mapper<CadastroBarbeiroForm, Pessoa> cadastroBarbeiroFormToPessoaMapper;
    private final Mapper<Pessoa, ClienteResponse> pessoaToClienteResponseMapper;
    private final Mapper<Pessoa, BarbeiroResponse> pessoaToBarbeiroResponseMapper;
    private final Mapper<MultipartFile, Arquivo> multipartFileToArquivoMapper;
    private final Mapper<Pessoa, PessoaResponse> pessoaToPessoaResponseMapper;
    private final Mapper<AtualizarDadosPessoaForm, Pessoa> atualizarDadosPessoaFormToPessoaMapper;

    @Transactional
    @PostMapping(value = "/cliente", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClienteResponse> cadastrarCliente(@RequestPart("json") CadastroClienteForm form,
                                                            @RequestPart(value = "file", required = false) MultipartFile file,
                                                            HttpServletRequest request) {
        logRequisicaoRecebida(request);

        Pessoa pessoa = cadastroClienteFormToPessoaMapper.map(form);
        Pessoa cliente = pessoaService.inserirDadosPessoa(pessoa, montarArquivo(file));

        return ResponseEntity.created(montarLocationHeader(cliente))
                .body(pessoaToClienteResponseMapper.map(cliente));
    }

    @Transactional
    @PostMapping(value = "/barbeiro", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BarbeiroResponse> cadastrarBarbeiro(@RequestPart("json") CadastroBarbeiroForm form,
                                                              @RequestPart(value = "file", required = false) MultipartFile file,
                                                              HttpServletRequest request) {
        logRequisicaoRecebida(request);

        Pessoa pessoa = cadastroBarbeiroFormToPessoaMapper.map(form);
        Pessoa barbeiro = pessoaService.inserirDadosPessoa(pessoa, montarArquivo(file));

        return ResponseEntity.created(montarLocationHeader(barbeiro))
                .body(pessoaToBarbeiroResponseMapper.map(barbeiro));
    }

    @GetMapping("/{id_pessoa}")
    public ResponseEntity<PessoaResponse> obterDadosPessoa(@PathVariable("id_pessoa") UUID idPessoa,
                                                           HttpServletRequest request) {
        logRequisicaoRecebida(request);
        Pessoa pessoa = pessoaService.obterDadosPessoa(idPessoa);
        return ResponseEntity.ok(pessoaToPessoaResponseMapper.map(pessoa));
    }

    @PutMapping("/{id_pessoa}")
    public ResponseEntity<PessoaResponse> atualizarDadosPessoa(@PathVariable("id_pessoa") UUID idPessoa,
                                                       @RequestPart("json") AtualizarDadosPessoaForm form,
                                                       @RequestPart(value = "file", required = false) MultipartFile file,
                                                       HttpServletRequest request) {
        logRequisicaoRecebida(request);
        Pessoa pessoa = atualizarDadosPessoaFormToPessoaMapper.map(form);
        Pessoa pessoaAtualizada = pessoaService.atualizarDadosPessoa(idPessoa, pessoa);
        return ResponseEntity.ok(pessoaToPessoaResponseMapper.map(pessoaAtualizada));
    }

    @PutMapping("/{id_pessoa}/desativacao")
    public ResponseEntity<Void> desativarPessoa(@PathVariable("id_pessoa") UUID idPessoa,
                                                         HttpServletRequest request) {
        logRequisicaoRecebida(request);

        pessoaService.desativarPessoa(idPessoa);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id_pessoa}")
    public ResponseEntity<Void> removerPessoa(@PathVariable("id_pessoa") UUID idPessoa,
                                                         HttpServletRequest request) {
        logRequisicaoRecebida(request);

        pessoaService.removerPessoa(idPessoa);
        return ResponseEntity.noContent().build();
    }

    private static URI montarLocationHeader(Pessoa pessoa) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/pessoas")
                .path("/{id_pessoa}")
                .buildAndExpand(pessoa.getId().toString())
                .toUri();
    }

    private Arquivo montarArquivo(MultipartFile file) {
        return file != null
                ? multipartFileToArquivoMapper.map(file)
                : null;
    }

}
