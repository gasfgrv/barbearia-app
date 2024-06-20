package com.gasfgrv.barbearia.application.service.usuario;

import com.gasfgrv.barbearia.adapter.token.TokenService;
import com.gasfgrv.barbearia.application.exception.usuario.UsuarioNaoEncontradoException;
import com.gasfgrv.barbearia.adapter.email.EmailAdapter;
import com.gasfgrv.barbearia.domain.entity.Usuario;

import com.gasfgrv.barbearia.domain.port.database.usuario.UsuarioRepositoryPort;
import com.gasfgrv.barbearia.domain.port.email.EmailPort;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepositoryPort usuarioRepository;
    private final Mapper<Usuario, UserDetails> mapper;
    private final EmailPort emailAdapter;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
            return mapper.map(getUsuario(login));
        } catch (Exception e) {
            log.error("Erro ao consultar: {}", e.getMessage());
            throw new UsernameNotFoundException("Erro ao procurar usuário");
        }
    }

    public void gerarTokenParaResetarSenhaUsuario(String login, String url, String token) {
        Usuario usuario = getUsuario(login);

        log.info("Criando token para reset da senha");
        tokenService.criarResetToken(usuario, token);

        log.info("Enviando token para reset da senha via e-mail");
        emailAdapter.enviarResetToken(usuario, url);
    }

    public void trocarSenha(String login, String senha) {
        Usuario usuario = getUsuario(login);
        usuario.setSenha(passwordEncoder.encode(senha));

        log.info("Salvando nova senha do usuário");
        usuarioRepository.salvarUsuario(usuario);
    }

    private Usuario getUsuario(String login) {
        log.info("Obtendo dados do usuario: {}", login);
        return Optional.ofNullable(usuarioRepository.findByLogin(login))
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }
}
