package com.luba.gpt.config;


import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
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
