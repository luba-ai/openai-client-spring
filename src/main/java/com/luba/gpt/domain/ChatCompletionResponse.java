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
public class ChatCompletionResponse {

    private String string;
    private String object;
    private Long created;
    private String model;
    private List<ChatCompletionResponseChoice> choices;

}
