package com.luba.gpt.client;

import com.luba.gpt.config.OpenAIConfig;
import com.luba.gpt.domain.ChatCompletionRequest;
import com.luba.gpt.domain.ChatCompletionResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class OpenAIClient {

    private final WebClient openAiWebClient;
    private final OpenAIConfig config;

    public ChatCompletionResponse create(ChatCompletionRequest request) {
        if (Objects.isNull(request.getTemperature())) {
            request.setTemperature(config.getTemperature());
        }
        return openAiWebClient
            .post()
            .uri("/chat/completions")
            .body(request, ChatCompletionRequest.class)
            .retrieve()
            .bodyToMono(ChatCompletionResponse.class)
            .block();
    }
}
