package com.gasfgrv.barbearia.adapter.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gasfgrv.barbearia.adapter.token.TokenService;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.port.database.usuario.UsuarioRepositoryPort;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URI;

import static com.gasfgrv.barbearia.adapter.utils.RequestUtils.capturarHeader;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepositoryPort repository;
    private final Mapper<Usuario, UserDetails> mapper;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws IOException {
        try {
            String tokenJWT = recuperarToken(request);
            adiconarTokenAoContexo(tokenJWT);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            tratarErro(request, response, e);
        }
    }

    private void adiconarTokenAoContexo(String tokenJWT) {
        if (tokenJWT != null) {
            String subject = tokenService.getSubject(tokenJWT);
            UserDetails usuario = mapper.map(repository.findByLogin(subject));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }

    private void tratarErro(HttpServletRequest request, HttpServletResponse response, Exception ex) throws IOException {
        HttpStatus status = FORBIDDEN;
        URI uri = URI.create(new ServletWebRequest(request).getRequest().getRequestURI());

        Problem problem = Problem.create()
                .withTitle("Erro ao autenticar usuário")
                .withDetail(ex.getMessage())
                .withStatus(status)
                .withInstance(uri);

        response.setStatus(status.value());
        response.setContentType(APPLICATION_PROBLEM_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(problem));
    }

    private String recuperarToken(HttpServletRequest request) {
        String authorizationHeader = capturarHeader(AUTHORIZATION, request);
        return authorizationHeader != null
                ? authorizationHeader.split("\\s")[1].trim()
                : null;
    }

}
