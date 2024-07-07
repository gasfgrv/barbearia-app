package com.gasfgrv.barbearia.adapter.email;

import lombok.Builder;

@Builder
public record DadosEmail(String from, String to, String subject, String htmlContent) {
}
