package com.gasfgrv.barbearia.application.service.usuario;

import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchema;
import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchemaMock;
import com.gasfgrv.barbearia.adapter.email.EmailAdapter;
import com.gasfgrv.barbearia.adapter.token.DadosToken;
import com.gasfgrv.barbearia.adapter.token.TokenService;
import com.gasfgrv.barbearia.application.exception.usuario.UsuarioNaoEncontradoException;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.entity.UsuarioMock;
import com.gasfgrv.barbearia.domain.port.database.usuario.UsuarioRepositoryPort;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepositoryPort repository;

    @Mock
    private Mapper<Usuario, UserDetails> mapper;

    @Mock
    private EmailAdapter emailAdapter;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private Clock clock;

    @InjectMocks
    private UsuarioService service;

    private Usuario usuario;
    private UsuarioSchema usuarioSchema;

    @BeforeEach
    void setUp() {
        usuario = UsuarioMock.montarUsuario();
        usuarioSchema = UsuarioSchemaMock.montarUsuarioSchema();
    }

    @Test
    @DisplayName("Deve obter um usuário existente na base de dados e retornar como um UserDetails")
    void deveObterUmUsuarioExistenteNaBaseDeDadosERetornarComoUmUserDetails() {
        when(repository.findByLogin(anyString())).thenReturn(usuario);
        when(mapper.map(any(Usuario.class))).thenReturn(usuarioSchema);

        UserDetails userDetails = service.loadUserByUsername("teste@teste.com");

        assertNotNull(userDetails);

        assertEquals(usuario.getLogin(), userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
        assertEquals(usuario.getPerfil().getNome(), userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList().get(0));
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando houver algum erro na consulta de usuário")
    void deveLancarUsernameNotFoundExceptionQuandoHouverAlgumErroNaConsultaDeUsuario() {
        when(repository.findByLogin(anyString())).thenThrow(new UsuarioNaoEncontradoException());
        assertThrowsExactly(UsernameNotFoundException.class, () -> service.loadUserByUsername("teste@teste.com"));
    }

    @Test
    @DisplayName("Deve criar e enviar um token para reset da senha")
    void deveCriarEEnviarUmTokenParaResetDaSenha() {
        ArgumentCaptor<DadosToken> dadosTokenCaptor = ArgumentCaptor.forClass(DadosToken.class);
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);

        when(repository.findByLogin(anyString())).thenReturn(usuario);
        when(tokenService.criarResetToken(dadosTokenCaptor.capture())).thenReturn(null);
        doNothing().when(emailAdapter).enviarResetToken(usuarioCaptor.capture(), anyString());

        ZonedDateTime now = ZonedDateTime.of(
                LocalDateTime.of(2020, 8, 8, 12, 45),
                ZoneOffset.of("-03:00"));

        when(clock.getZone()).thenReturn(now.getZone());
        when(clock.instant()).thenReturn(now.toInstant());

        service.gerarTokenParaResetarSenhaUsuario(usuario.getLogin(), "http://localhost/teste");

        assertEquals(usuario.getLogin(), dadosTokenCaptor.getValue().subject());
    }

    @Test
    @DisplayName("Deve trocar a senha do usuário")
    void deveTrocarASenhaDoUsuario() {
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        String senhaAntiga = usuario.getSenha();

        when(repository.findByLogin(anyString())).thenReturn(usuario);
        when(encoder.encode(anyString())).thenReturn("$2y$10$7TIzNabfy6J4in2DfbbGKe1ID/Cc9H6g6DVzoooMZv1yLrwS/cthq");
        doNothing().when(repository).salvarUsuario(usuarioCaptor.capture());

        service.trocarSenha(usuario.getLogin(), "teste");

        assertEquals(usuario.getLogin(), usuarioCaptor.getValue().getLogin());
        assertNotEquals(senhaAntiga, usuarioCaptor.getValue().getSenha());
    }

}