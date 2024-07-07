package com.gasfgrv.barbearia.adapter.database.perfil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;

import java.util.Optional;

public interface PerfilJpaRepository extends JpaRepository<PerfilSchema, Integer> {

    Optional<GrantedAuthority> findByNome(String nome);

}
