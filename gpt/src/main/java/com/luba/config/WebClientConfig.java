package com.luba.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
class WebClientConfig {

    private final OpenAIConfig config;

    @Bean
    public WebClient openAiWebClient(ObjectMapper objectMapper) {
        return WebClient
            .builder()
            .baseUrl(config.getUrl())
            .codecs(clientDefaultCodecsConfigurer -> {
                clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                clientDefaultCodecsConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
            })
            .defaultHeader("Authorization", "Bearer " + config.getKey())
            .build();
    }
}
