package com.luba.gpt.service;

import com.luba.gpt.domain.ChatCompletionMessage;
import com.luba.gpt.domain.ChatCompletionRequest;
import com.luba.gpt.domain.ChatCompletionResponse;
import java.util.List;
import lombok.NonNull;

public interface GPTService {

    @NonNull
    ChatCompletionResponse completion(@NonNull ChatCompletionRequest request);

    @NonNull
    ChatCompletionResponse completion(@NonNull List<ChatCompletionMessage> messages);

}
