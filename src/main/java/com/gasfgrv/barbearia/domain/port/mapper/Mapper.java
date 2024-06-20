package com.gasfgrv.barbearia.domain.port.mapper;

public interface Mapper<I, O> {

    O map(I input);

}
