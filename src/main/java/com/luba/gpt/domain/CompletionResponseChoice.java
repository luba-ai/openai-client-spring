package com.luba.gpt.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletionResponseChoice {
    private int index;
    private CompletionMessageResponse message;
}
