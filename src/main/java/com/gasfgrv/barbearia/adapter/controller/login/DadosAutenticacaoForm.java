package com.gasfgrv.barbearia.adapter.controller.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DadosAutenticacaoForm {

    @Email
    private String login;

    @NotBlank
    private String senha;

}
