package com.gasfgrv.barbearia.application.service.usuario;

import com.gasfgrv.barbearia.adapter.token.TokenService;
import com.gasfgrv.barbearia.application.exception.usuario.UsuarioNaoEncontradoException;
import com.gasfgrv.barbearia.application.service.email.EmailService;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.port.database.reset.PasswordResetTokenRepositoryPort;
import com.gasfgrv.barbearia.port.database.usuario.UsuarioRepositoryPort;
import com.gasfgrv.barbearia.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepositoryPort usuarioRepository;
    private final Mapper<Usuario, UserDetails> mapper;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(username);
        return mapper.map(usuario);
    }

    public void resetarSenhaUsuario(String login, String url, String token) {
        Usuario usuario = getUsuario(login);
        tokenService.criarResetToken(usuario, token);
        emailService.enviarResetToken(usuario, url);
    }

    public void trocarSenha(String subject, String senha) {
        Usuario usuario = getUsuario(subject);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuarioRepository.salvarUsuario(usuario);
    }

    private Usuario getUsuario(String login) {
        return Optional.ofNullable(usuarioRepository.findByLogin(login))
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }
}
