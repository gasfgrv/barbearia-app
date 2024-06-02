package com.gasfgrv.barbearia.adapter.database.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioSchema, String> {
    UserDetails findByLogin(String login);
}
