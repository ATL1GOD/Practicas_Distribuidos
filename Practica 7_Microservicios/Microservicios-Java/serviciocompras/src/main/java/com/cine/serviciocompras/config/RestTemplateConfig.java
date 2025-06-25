package com.cine.serviciocompras.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    private final RestTemplateInterceptor restTemplateInterceptor;

    public RestTemplateConfig(RestTemplateInterceptor restTemplateInterceptor) {
        this.restTemplateInterceptor = restTemplateInterceptor;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of(restTemplateInterceptor));
        return restTemplate;
    }
}
