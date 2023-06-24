package com.luba.gpt.service.impl;

import com.luba.gpt.client.OpenAIClient;
import com.luba.gpt.domain.ChatCompletionRequest;
import com.luba.gpt.domain.ChatCompletionResponse;
import com.luba.gpt.service.GPTService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GPTServiceImpl implements GPTService {

    private final OpenAIClient gptClient;

    @Override
    public @NonNull ChatCompletionResponse completion(@NonNull ChatCompletionRequest request) {
        return gptClient.create(request);
    }
}
