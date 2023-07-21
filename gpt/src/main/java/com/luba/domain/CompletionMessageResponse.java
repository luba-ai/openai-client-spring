package com.luba.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompletionMessageResponse {

    @NotNull
    private Role role;
    private String content;
    @JsonProperty("function_call")
    private FunctionCallResponse functionCall;
}
