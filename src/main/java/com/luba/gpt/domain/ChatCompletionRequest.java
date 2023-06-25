package com.luba.gpt.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionRequest {

    // todo: convert model to use an enum instead of a string
    private String model;
    private List<ChatCompletionMessage> messages;
    private Double temperature;
}
