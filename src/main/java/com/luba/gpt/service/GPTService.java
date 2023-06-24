package com.luba.gpt.service;

import com.luba.gpt.domain.ChatCompletionRequest;
import com.luba.gpt.domain.ChatCompletionResponse;
import lombok.NonNull;

public interface GPTService {

    @NonNull
    ChatCompletionResponse completion(@NonNull ChatCompletionRequest request);

}
