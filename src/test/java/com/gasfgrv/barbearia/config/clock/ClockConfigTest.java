package com.gasfgrv.barbearia.config.clock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ClockConfig.class)
class ClockConfigTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("Deve criar um bean de Clock")
    void deveCriarUmBeanDeClock() {
        Clock bean = applicationContext.getBean(Clock.class);
        assertInstanceOf(Clock.class, bean);
        assertNotNull(bean);
    }

}