package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchema;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioToUserDetailsMapper implements Mapper<Usuario, UserDetails> {

    private final ModelMapper mapper;

    @Override
    public UserDetails map(Usuario input) {
        return mapper.map(input, UsuarioSchema.class);
    }

}
