package com.gasfgrv.barbearia.port.mapper;

public interface Mapper<I, O> {
    O map(I input);
}
