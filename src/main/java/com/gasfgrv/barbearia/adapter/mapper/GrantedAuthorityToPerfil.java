package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.domain.entity.Perfil;
import com.gasfgrv.barbearia.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GrantedAuthorityToPerfil implements Mapper<GrantedAuthority, Perfil> {

    private final ModelMapper mapper;

    @Override
    public Perfil map(GrantedAuthority input) {
        return mapper.map(input, Perfil.class);
    }

}
