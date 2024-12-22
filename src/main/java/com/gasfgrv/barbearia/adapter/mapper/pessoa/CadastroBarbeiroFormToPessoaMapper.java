package com.gasfgrv.barbearia.adapter.mapper.pessoa;

import com.gasfgrv.barbearia.adapter.controller.pessoa.CadastroBarbeiroForm;
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
public class CadastroBarbeiroFormToPessoaMapper implements Mapper<CadastroBarbeiroForm, Pessoa> {

    private final ModelMapper modelMapper;
    private final PerfilRepositoryPort perfilRepository;

    @Override
    public Pessoa map(CadastroBarbeiroForm input) {
        return modelMapper.map(input, Pessoa.class, "CadastroBarbeiroFormToPessoaMapper");
    }

    @PostConstruct
    private void configurarMapper() {
        modelMapper.typeMap(CadastroBarbeiroForm.class, Pessoa.class, "CadastroBarbeiroFormToPessoaMapper")
                .addMappings(mapper -> {
                    mapper.<String>map(form -> form.getUsuario().getLogin(),
                            (pessoa, login) -> pessoa.getUsuario().setLogin(login));
                    mapper.<String>map(form -> form.getUsuario().getSenha(),
                            (pessoa, senha) -> pessoa.getUsuario().setSenha(senha));
                    mapper.<Perfil>map(perfil -> perfilRepository.findByNome("barbeiro"),
                            (pessoa, perfil) -> pessoa.getUsuario().setPerfil(perfil));
                });
    }

}
