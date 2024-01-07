package com.example.confing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Lb9Config {

    @Bean
    public WebClient.Builder webclintBuilder(){
        return WebClient.builder();
    }


}
