package com.gasfgrv.barbearia.application.service.pessoa;

import com.gasfgrv.barbearia.application.exception.pessoa.IdadeInvalidaException;
import com.gasfgrv.barbearia.application.exception.pessoa.PessoaExistenteException;
import com.gasfgrv.barbearia.application.service.usuario.UsuarioService;
import com.gasfgrv.barbearia.domain.entity.Arquivo;
import com.gasfgrv.barbearia.domain.entity.Pessoa;
import com.gasfgrv.barbearia.domain.port.bucket.BucketPort;
import com.gasfgrv.barbearia.domain.port.database.pessoa.PessoaRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PessoaService {

    private final Clock clock;
    private final PessoaRepositoryPort pessoaRepository;
    private final UsuarioService usuarioService;
    private final BucketPort bucket;

    public Pessoa inserirDadosPessoa(Pessoa pessoa, Arquivo arquivo) {
        boolean pessoaExiste = pessoaRepository.existeCpfSalvo(pessoa.getCpf());

        if (pessoaExiste) {
            log.error("CPF já salvo");
            throw new PessoaExistenteException();
        }

        long idadePessoa = ChronoUnit.YEARS
                .between(pessoa.getDataNascimento(), LocalDate.now(clock));

        if (idadePessoa < 18) {
            log.error("Idade inválida para cadastro");
            throw new IdadeInvalidaException();
        }

        usuarioService.salvarUsuario(pessoa.getUsuario());

        pessoa.setAtivo(true);

        UUID randomUUID = UUID.randomUUID();
        pessoa.setId(randomUUID);

        if (arquivo != null) {
            arquivo.setIdImagem(randomUUID);
            bucket.salvar(arquivo);
            pessoa.setImagem(arquivo);
        }

        return pessoaRepository.salvarPessa(pessoa);
    }

    public Pessoa obterDadosPessoa(UUID idPessoa) {
        return pessoaRepository.buscarPessoa(idPessoa);
    }

    public Pessoa atualizarDadosPessoa(UUID idPessoa, Pessoa pessoa) {
        Pessoa dadosSalvos = pessoaRepository.buscarPessoa(idPessoa);
        dadosSalvos.setNome(pessoa.getNome());
        dadosSalvos.setCelular(pessoa.getCelular());
        return pessoaRepository.salvarPessa(dadosSalvos);
    }

    public void desativarPessoa(UUID idPessoa) {
        Pessoa dadosSalvos = pessoaRepository.buscarPessoa(idPessoa);
        dadosSalvos.setAtivo(false);
        pessoaRepository.salvarPessa(dadosSalvos);
    }

    public void removerPessoa(UUID idPessoa) {
        pessoaRepository.removerPessoa(idPessoa);
    }
}
