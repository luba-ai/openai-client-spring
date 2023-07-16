package com.luba.gpt.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
class WebClientConfig {

    private final OpenAIConfig config;

    @Bean
    public WebClient openAiWebClient() {
        return WebClient
            .builder()
            .baseUrl(config.getUrl())
            .defaultHeader("Authorization", "Bearer " + config.getKey())
            .build();
    }
}
