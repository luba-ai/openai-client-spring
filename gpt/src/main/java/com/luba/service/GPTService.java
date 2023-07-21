package com.luba.service;

import com.luba.domain.CompletionMessage;
import com.luba.domain.CompletionRequest;
import com.luba.domain.CompletionResponse;
import java.util.List;
import lombok.NonNull;

public interface GPTService {

    @NonNull
    CompletionResponse completion(@NonNull CompletionRequest request);

    @NonNull
    CompletionResponse completion(@NonNull List<CompletionMessage> messages);

}
