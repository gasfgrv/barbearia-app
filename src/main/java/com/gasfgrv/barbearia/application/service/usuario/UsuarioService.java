package com.gasfgrv.barbearia.application.service.usuario;

import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.port.database.usuario.UsuarioRepository;
import com.gasfgrv.barbearia.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final Mapper<Usuario, UserDetails> mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repository.findByLogin(username);
        return mapper.map(usuario);
    }

}
