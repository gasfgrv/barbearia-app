package com.gasfgrv.barbearia.adapter.database.reset;

import com.gasfgrv.barbearia.adapter.database.usuario.UsuarioSchema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "password_reset_token")
public class PasswordResetTokenSchema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String token;

    @OneToOne
    @JoinColumn(name = "usuario_login", referencedColumnName = "login")
    private UsuarioSchema usuarioLogin;

    private LocalDateTime expiryDate;

}
