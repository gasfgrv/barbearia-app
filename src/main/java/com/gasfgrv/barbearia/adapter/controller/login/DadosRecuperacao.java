package com.gasfgrv.barbearia.adapter.controller.login;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class DadosRecuperacao {

    @Email
    private String login;

}
