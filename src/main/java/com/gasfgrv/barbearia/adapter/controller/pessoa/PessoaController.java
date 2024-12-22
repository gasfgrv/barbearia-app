package com.gasfgrv.barbearia.adapter.controller.pessoa;

import com.gasfgrv.barbearia.application.service.pessoa.PessoaService;
import com.gasfgrv.barbearia.domain.entity.Arquivo;
import com.gasfgrv.barbearia.domain.entity.Pessoa;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.gasfgrv.barbearia.adapter.utils.RequestUtils.logRequisicaoRecebida;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;
    private final Mapper<CadastroClienteForm, Pessoa> cadastroClienteFormToPessoaMapper;
    private final Mapper<Pessoa, ClienteResponse> pessoaToClienteResponseMapper;
    private final Mapper<MultipartFile, Arquivo> multipartFileToArquivoMapper;

    @Transactional
    @PostMapping(value = "/cliente", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ClienteResponse> cadastrarCliente(@RequestPart("json") CadastroClienteForm form,
                                                            @RequestPart(value = "file", required = false) MultipartFile file,
                                                            HttpServletRequest request) {
        logRequisicaoRecebida(request);

        Pessoa pessoa = cadastroClienteFormToPessoaMapper.map(form);
        Arquivo arquivo = file != null
                ? multipartFileToArquivoMapper.map(file)
                : null;

        Pessoa cliente = pessoaService.inserirDadosPessoa(pessoa, arquivo);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment(cliente.getId().toString())
                .build()
                .toUri();

        return ResponseEntity.created(location)
                .body(pessoaToClienteResponseMapper.map(cliente));
    }

}
