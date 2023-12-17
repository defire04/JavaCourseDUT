package com.example.confing;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Lb8Config {

    @Bean
    public WebClient.Builder webclintBuilder(){
        return WebClient.builder();
    }


    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
