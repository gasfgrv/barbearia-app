package com.gasfgrv.barbearia.adapter.database.reset;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenJpaRepository extends JpaRepository<PasswordResetTokenSchema, Integer> {
    Optional<PasswordResetTokenSchema> findByToken(String token);
}
