package com.luba.gpt.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.awt.print.Book;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletionMessage {

    @NotNull
    private Role role;
    @NotNull
    private String content;
}
