package com.luba.gpt.service.impl;

import com.luba.gpt.client.OpenAIClient;
import com.luba.gpt.config.OpenAIConfig;
import com.luba.gpt.domain.CompletionMessage;
import com.luba.gpt.domain.CompletionRequest;
import com.luba.gpt.domain.CompletionResponse;
import com.luba.gpt.service.GPTService;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class GPTServiceImpl implements GPTService {

    private final OpenAIClient gptClient;
    private final OpenAIConfig config;

    @Override
    public @NonNull CompletionResponse completion(@NonNull CompletionRequest request) {
        return gptClient.create(request);
    }

    @Override
    public @NonNull CompletionResponse completion(@NonNull List<CompletionMessage> messages) {
        CompletionRequest request = createRequestWithDefaults(messages);
        return gptClient.create(request);
    }

    private CompletionRequest createRequestWithDefaults(List<CompletionMessage> messages) {
        CompletionRequest request = new CompletionRequest();
        request.setMessages(messages);
        request.setModel(config.getModel());
        request.setTemperature(config.getTemperature());
        return request;
    }
}
