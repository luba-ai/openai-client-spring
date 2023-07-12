package com.luba.gpt.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = ChatCompletionRequestSerializer.class)
public class ChatCompletionRequest {

    private String model;
    private List<ChatCompletionMessage> messages;
    private double temperature;
    private Class<? extends IBaseFunction> functions;
    @JsonProperty("function_call")
    private FunctionCall functionCall;
}
