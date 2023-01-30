package com.example.springdeepdive;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class TestConfiguration {



    @Bean
    @Scope(scopeName = "prototype")
    public Cat cat(){
        return new Cat("Test");
    }
}
