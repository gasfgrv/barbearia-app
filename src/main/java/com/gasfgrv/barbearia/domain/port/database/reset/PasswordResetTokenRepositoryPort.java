package com.gasfgrv.barbearia.domain.port.database.reset;

import com.gasfgrv.barbearia.domain.entity.Usuario;

import java.time.LocalDateTime;

public interface PasswordResetTokenRepositoryPort {

    void salvarResetToken(String login, LocalDateTime dataGeracao, String token);

    boolean existeTokenParaAtualizarSenha(String token);

    LocalDateTime obterExpiracaoToken(String token);

}
