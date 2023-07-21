package com.luba.client;

import com.luba.domain.CompletionRequest;
import com.luba.domain.CompletionResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class OpenAIClient {

    private final WebClient openAiWebClient;

    public CompletionResponse create(@NonNull CompletionRequest request) {
        return openAiWebClient
            .post()
            .uri("/chat/completions")
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(CompletionResponse.class)
            .block();
    }
}
