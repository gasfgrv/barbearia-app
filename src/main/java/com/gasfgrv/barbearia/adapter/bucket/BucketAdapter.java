package com.gasfgrv.barbearia.adapter.bucket;

import com.gasfgrv.barbearia.application.exception.bucket.BucketException;
import com.gasfgrv.barbearia.domain.entity.Arquivo;
import com.gasfgrv.barbearia.domain.port.bucket.BucketPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Component
@RequiredArgsConstructor
public class BucketAdapter implements BucketPort {

    private static final String BUCKET_NAME = "barbearia.app.bucket";
    private final S3AsyncClient s3Client;

    @Override
    public void salvar(Arquivo arquivo) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(arquivo.getNome())
                .build();
        AsyncRequestBody requestBody = AsyncRequestBody.fromBytes(arquivo.getBytes());
        s3Client.putObject(putObjectRequest, requestBody)
                .thenAccept(response -> log.info("Dados imagem salva no bucket: {}", response.eTag()))
                .exceptionally(e -> {
                    throw new BucketException(e.getMessage());
                });
    }

}
