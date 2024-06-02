package com.gasfgrv.barbearia.adapter.bucket;

import com.gasfgrv.barbearia.port.bucket.BucketPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

@Component
@RequiredArgsConstructor
public class BucketAdapter implements BucketPort {

    private final S3Client s3Client;

}
