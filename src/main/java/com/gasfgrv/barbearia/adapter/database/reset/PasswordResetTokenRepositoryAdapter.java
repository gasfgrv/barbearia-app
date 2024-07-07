package com.gasfgrv.barbearia.adapter.database.reset;

import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchema;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.port.database.reset.PasswordResetTokenRepositoryPort;
import com.gasfgrv.barbearia.domain.port.database.usuario.UsuarioRepositoryPort;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepositoryPort {

    private final PasswordResetTokenJpaRepository repository;
    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final Mapper<Usuario, UserDetails> mapper;

    @Override
    public void salvarResetToken(String login, LocalDateTime dataGeracao, String token) {
        log.info("Salvando dados do token na base dade dados");
        PasswordResetTokenSchema schema = new PasswordResetTokenSchema();
        schema.setToken(token);
        Usuario usuario = usuarioRepositoryPort.findByLogin(login);
        schema.setUsuarioLogin((UsuarioSchema) mapper.map(usuario));
        schema.setExpiryDate(dataGeracao.plusHours(2));
        repository.save(schema);
    }

    @Override
    public boolean existeTokenParaAtualizarSenha(String token) {
        log.info("Verificando a existencia do token na base de dados");
        return repository.findByToken(token).isPresent();
    }

    @Override
    public LocalDateTime obterExpiracaoToken(String token) {
        log.info("Obtendo a data de expiração do token");
        return repository.findByToken(token)
                .map(PasswordResetTokenSchema::getExpiryDate)
                .orElse(LocalDateTime.MIN);
    }

}
