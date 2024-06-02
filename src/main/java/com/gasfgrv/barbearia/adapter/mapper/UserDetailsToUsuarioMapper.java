package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsToUsuarioMapper implements Mapper<UserDetails, Usuario> {

    private final ModelMapper mapper;

    @Override
    public Usuario map(UserDetails input) {
        return mapper.map(input, Usuario.class);
    }
}
