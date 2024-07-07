package com.gasfgrv.barbearia.adapter.controller.login;

import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchema;
import com.gasfgrv.barbearia.adapter.token.DadosToken;
import com.gasfgrv.barbearia.adapter.token.TokenService;
import com.gasfgrv.barbearia.application.service.usuario.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/v1/login")
@RequiredArgsConstructor
public class AutenticacaoController {

    private final Clock clock;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados, HttpServletRequest request) {
        logRequisicaoRecebida(request);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dados.getLogin(), dados.getSenha());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UsuarioSchema principal = (UsuarioSchema) authentication.getPrincipal();
        String tokenJwt = tokenService.gerarToken(new DadosToken(principal.getLogin(), LocalDateTime.now(clock)));
        return ResponseEntity.ok(new DadosTokenJWT(tokenJwt));
    }

    @PostMapping("/reset")
    public ResponseEntity<DadosTokenJWT> gerarEEnviarResetToken(@RequestBody @Valid DadosRecuperacao dados, HttpServletRequest request) {
        logRequisicaoRecebida(request);
        String url = request.getRequestURL().toString();
        String token = usuarioService.gerarTokenParaResetarSenhaUsuario(dados.getLogin(), url);
        return ResponseEntity.ok(new DadosTokenJWT(token));
    }

    @Transactional
    @PutMapping("/reset/nova")
    public ResponseEntity<Void> alterarSenha(@RequestBody @Valid NovaSenha form, HttpServletRequest request) {
        logRequisicaoRecebida(request);
        String token = Objects.requireNonNull(request.getHeader("Authorization")).split("\\s")[1].trim();
        tokenService.validarResetToken(token);
        usuarioService.trocarSenha(tokenService.getSubject(token), form.getSenha());
        return ResponseEntity.noContent().build();
    }

    private void logRequisicaoRecebida(HttpServletRequest request) {
        log.info("Requisição recebida para: [{}] - {}", request.getMethod(), request.getServletPath());
    }

}
