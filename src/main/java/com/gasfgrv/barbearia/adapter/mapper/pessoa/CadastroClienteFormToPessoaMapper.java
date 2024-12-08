package com.gasfgrv.barbearia.adapter.mapper.pessoa;

import com.gasfgrv.barbearia.adapter.controller.pessoa.CadastroClienteForm;
import com.gasfgrv.barbearia.domain.entity.Perfil;
import com.gasfgrv.barbearia.domain.entity.Pessoa;
import com.gasfgrv.barbearia.domain.port.database.perfil.PerfilRepositoryPort;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CadastroClienteFormToPessoaMapper implements Mapper<CadastroClienteForm, Pessoa> {

    private final ModelMapper modelMapper;
    private final PerfilRepositoryPort perfilRepository;

    @Override
    public Pessoa map(CadastroClienteForm input) {
        return modelMapper.map(input, Pessoa.class, "CadastroClienteFormToPessoaMapper");
    }

    @PostConstruct
    private void configureMapper() {
        modelMapper.typeMap(CadastroClienteForm.class, Pessoa.class, "CadastroClienteFormToPessoaMapper")
                .addMappings(mapper -> {
                    mapper.<String>map(form -> form.getUsuario().getLogin(),
                            (pessoa, login) -> pessoa.getUsuario().setLogin(login));
                    mapper.<String>map(form -> form.getUsuario().getSenha(),
                            ((pessoa, senha) -> pessoa.getUsuario().setSenha(senha)));
                    mapper.<Perfil>map(perfil -> perfilRepository.findByNome("cliente"),
                            (pessoa, perfil) -> pessoa.getUsuario().setPerfil(perfil));
                });
    }

}
