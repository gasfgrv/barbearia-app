package com.gasfgrv.barbearia.adapter.database.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioSchema, String> {

    Optional<UserDetails> findByLogin(String login);

}
