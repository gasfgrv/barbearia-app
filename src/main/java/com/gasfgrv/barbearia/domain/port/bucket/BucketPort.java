package com.gasfgrv.barbearia.domain.port.bucket;

import com.gasfgrv.barbearia.domain.entity.Arquivo;

public interface BucketPort {
    void salvar(Arquivo arquivo);
}
