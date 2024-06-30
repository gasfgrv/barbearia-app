package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.database.perfil.PerfilSchemaMock;
import com.gasfgrv.barbearia.config.mapper.ModelMapperConfig;
import com.gasfgrv.barbearia.domain.entity.Perfil;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GrantedAuthorityToPerfil.class, ModelMapperConfig.class})
class GrantedAuthorityToPerfilTest {

    @Autowired
    Mapper<GrantedAuthority, Perfil> mapper;

    @Test
    @DisplayName("Deve converter um objeto GrantedAuthority para Perfil")
    void deveConverterUmObjetoUsuarioParaUserDetails() {
        GrantedAuthority authority = PerfilSchemaMock.montarPerfilSchema();
        Perfil perfil = mapper.map(authority);
        assertNotNull(perfil);
    }

}