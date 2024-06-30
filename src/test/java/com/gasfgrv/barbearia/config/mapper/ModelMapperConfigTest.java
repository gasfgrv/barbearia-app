package com.gasfgrv.barbearia.config.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ModelMapperConfig.class)
class ModelMapperConfigTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("Deve criar um bean de ModelMapper")
    void deveCriarUmBeanDeModelMapper() {
        ModelMapper bean = applicationContext.getBean(ModelMapper.class);
        assertInstanceOf(ModelMapper.class, bean);
        assertNotNull(bean);
    }

}