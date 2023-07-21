package com.luba.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.luba.config.OpenAIConfig;
import com.luba.parser.service.GPTFunctionParser;
import com.luba.service.TokenizationService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompletionRequestSerializer extends JsonSerializer<CompletionRequest> {

    private final GPTFunctionParser functionParser;

    private final TokenizationService tokenizationService;

    private final OpenAIConfig config;

    public CompletionRequestSerializer(GPTFunctionParser functionParser, TokenizationService tokenizationService, OpenAIConfig config) {
        this.functionParser = functionParser;
        this.tokenizationService = tokenizationService;
        this.config = config;
    }

    @Override
    public void serialize(CompletionRequest chatCompletionRequest, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("model", chatCompletionRequest.getModel());
        jsonGenerator.writeNumberField("temperature", chatCompletionRequest.getTemperature());
        jsonGenerator.writeArrayFieldStart("messages");

        Integer tokenCount = 0;
        List<CompletionMessage> messages = chatCompletionRequest.getMessages();
        if (messages != null && !messages.isEmpty()) {

            List<CompletionMessage> reversedList = new ArrayList<>(messages);
            Collections.reverse(reversedList);

            for (CompletionMessage message : reversedList) {
                tokenCount += tokenizationService.getTokenCount(message);
                if (tokenCount >= config.getTokenLimit()) {
                    break;
                }
                jsonGenerator.writeObject(message);
            }
        }
        jsonGenerator.writeEndArray();

        if (chatCompletionRequest.getFunctions() != null) {
            jsonGenerator.writeObjectField("functions", this.functionParser.extractFunctions(chatCompletionRequest.getFunctions()));
        }

        if (chatCompletionRequest.getFunctionCall() != null) {
            jsonGenerator.writeObjectField("function_call", chatCompletionRequest.getFunctionCall());
        }
        jsonGenerator.writeEndObject();
    }
}
