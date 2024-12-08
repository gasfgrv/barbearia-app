package com.gasfgrv.barbearia.adapter.mapper.arquivo;

import com.gasfgrv.barbearia.application.exception.arquivo.ErroProcesaamentoImagemException;
import com.gasfgrv.barbearia.domain.entity.Arquivo;
import com.gasfgrv.barbearia.domain.port.mapper.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class MultipartFileToArquivoMapper implements Mapper<MultipartFile, Arquivo> {

    @Override
    public Arquivo map(MultipartFile input) {
        try {
            return Arquivo.builder()
                    .nome(input.getOriginalFilename())
                    .tipo(input.getContentType())
                    .bytes(input.getBytes())
                    .build();
        } catch (IOException e) {
            throw new ErroProcesaamentoImagemException(e.getMessage());
        }
    }

}
