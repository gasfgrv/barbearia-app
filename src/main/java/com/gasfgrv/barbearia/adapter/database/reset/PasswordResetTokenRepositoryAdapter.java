package com.gasfgrv.barbearia.adapter.database.reset;

import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchema;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.port.database.reset.PasswordResetTokenRepositoryPort;
import com.gasfgrv.barbearia.port.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepositoryPort {

    private final PasswordResetTokenJpaRepository repository;
    private final Mapper<Usuario, UserDetails> mapper;

    @Override
    public void salvarResetToken(Usuario usuario, String token) {
        PasswordResetTokenSchema schema = new PasswordResetTokenSchema();
        schema.setToken(token);
        schema.setUsuarioLogin((UsuarioSchema) mapper.map(usuario));
        schema.setExpiryDate(LocalDateTime.now().plusHours(2));
        repository.save(schema);
    }

    @Override
    public boolean existeTokenParaAtualizarSenha(String token) {
        return Optional.ofNullable(repository.findByToken(token)).isPresent();
    }

    @Override
    public LocalDateTime obterExpiracaoToken(String token) {
        return repository.findByToken(token).map(PasswordResetTokenSchema::getExpiryDate).orElse(LocalDateTime.MIN);
    }

}
