package com.gasfgrv.barbearia.adapter.database.usuario;

import com.gasfgrv.barbearia.adapter.database.perfil.PerfilSchema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

@Data
@Entity
@Table(name = "usuario")
public class UsuarioSchema implements UserDetails, Serializable {

    @Id
    private String login;

    private String senha;

    @OneToOne
    @JoinColumn(name = "perfil_id", referencedColumnName = "id")
    private PerfilSchema perfil;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(perfil);
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
