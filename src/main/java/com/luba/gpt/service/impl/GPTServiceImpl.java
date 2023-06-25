package com.luba.gpt.service.impl;

import com.luba.gpt.client.OpenAIClient;
import com.luba.gpt.config.OpenAIConfig;
import com.luba.gpt.domain.ChatCompletionMessage;
import com.luba.gpt.domain.ChatCompletionRequest;
import com.luba.gpt.domain.ChatCompletionResponse;
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
    public @NonNull ChatCompletionResponse completion(@NonNull ChatCompletionRequest request) {
        return gptClient.create(request);
    }

    @Override
    public @NonNull ChatCompletionResponse completion(@NonNull List<ChatCompletionMessage> messages) {
        ChatCompletionRequest request = createRequestWithDefaults(messages);
        return gptClient.create(request);
    }

    private ChatCompletionRequest createRequestWithDefaults(List<ChatCompletionMessage> messages) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setMessages(messages);
        request.setModel(config.getModel());
        request.setTemperature(config.getTemperature());
        return request;
    }
}
