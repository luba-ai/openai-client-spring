package com.luba.gpt.client;

import com.luba.gpt.config.OpenAIConfig;
import com.luba.gpt.domain.ChatCompletionRequest;
import com.luba.gpt.domain.ChatCompletionResponse;
import java.util.Objects;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class OpenAIClient {

    private final WebClient openAiWebClient;

    public ChatCompletionResponse create(@NonNull ChatCompletionRequest request) {
        return openAiWebClient
            .post()
            .uri("/chat/completions")
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(ChatCompletionResponse.class)
            .block();
    }
}
