package com.gasfgrv.barbearia.adapter.database.perfil;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;

public interface PerfilJpaRepository extends JpaRepository<PerfilSchema, Integer> {
    GrantedAuthority findByNome(String nome);
}
