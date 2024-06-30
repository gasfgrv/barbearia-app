package com.gasfgrv.barbearia.adapter.mapper;

import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchemaMock;
import com.gasfgrv.barbearia.config.mapper.ModelMapperConfig;
import com.gasfgrv.barbearia.domain.entity.Usuario;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {UserDetailsToUsuarioMapper.class, ModelMapperConfig.class})
class UserDetailsToUsuarioMapperTest {

    @Autowired
    Mapper<UserDetails, Usuario> mapper;

    @Test
    @DisplayName("Deve converter um objeto UserDetails para Usuario")
    void deveConverterUmObjetoUserDetailsParaUsuario() {
        UserDetails userDetails = UsuarioSchemaMock.montarUsuarioSchema();
        Usuario usuario = mapper.map(userDetails);
        assertNotNull(usuario);
        assertInstanceOf(Usuario.class, usuario);
    }

}