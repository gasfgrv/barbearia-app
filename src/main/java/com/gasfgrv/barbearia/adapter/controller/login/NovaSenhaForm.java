package com.gasfgrv.barbearia.adapter.controller.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NovaSenhaForm {

    @NotBlank
    private String senha;

}
