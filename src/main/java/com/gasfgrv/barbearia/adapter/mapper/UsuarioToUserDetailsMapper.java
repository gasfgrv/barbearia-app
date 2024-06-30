package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchema;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UsuarioToUserDetailsMapper implements Mapper<Usuario, UserDetails> {

    private final ModelMapper mapper;

    @Override
    public UserDetails map(Usuario input) {
        log.info("Convertendo para '{}'", UserDetails.class.getSimpleName());
        return mapper.map(input, UsuarioSchema.class);
    }

}
