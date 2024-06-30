package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.domain.entity.Perfil;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GrantedAuthorityToPerfil implements Mapper<GrantedAuthority, Perfil> {

    private final ModelMapper mapper;

    @Override
    public Perfil map(GrantedAuthority input) {
        log.info("Convertendo para '{}'", Perfil.class.getSimpleName());
        return mapper.map(input, Perfil.class);
    }

}
