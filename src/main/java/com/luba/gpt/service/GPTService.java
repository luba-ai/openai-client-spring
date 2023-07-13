package com.luba.gpt.service;

import com.luba.gpt.domain.CompletionMessage;
import com.luba.gpt.domain.CompletionRequest;
import com.luba.gpt.domain.CompletionResponse;
import java.util.List;
import lombok.NonNull;

public interface GPTService {

    @NonNull
    CompletionResponse completion(@NonNull CompletionRequest request);

    @NonNull
    CompletionResponse completion(@NonNull List<CompletionMessage> messages);

}
