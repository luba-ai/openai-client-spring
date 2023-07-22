package com.luba.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.luba.config.OpenAIConfig;
import com.luba.parser.service.GPTFunctionParser;
import com.luba.service.TokenizationService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompletionRequestSerializer extends StdSerializer<CompletionRequest> {

    private final GPTFunctionParser functionParser;

    private final TokenizationService tokenizationService;

    private final OpenAIConfig config;

    public CompletionRequestSerializer(GPTFunctionParser functionParser, TokenizationService tokenizationService, OpenAIConfig config) {
        super(CompletionRequest.class);
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


            // TODO: optimize this
            // we want to limit the number of tokens we send to OpenAI
            // so we need to count the tokens in the messages
            // remove the oldest message (first in the array)
            List<CompletionMessage> messagesCopy = new ArrayList<>(messages);
            tokenCount = tokenizationService.getTokenCount(messagesCopy);
            int i = messages.size();
            while (tokenCount >= config.getTokenLimit() && !messagesCopy.isEmpty()) {
                messagesCopy.remove(--i);
                tokenCount -= tokenizationService.getTokenCount(messagesCopy);
            }

            for (CompletionMessage message : messagesCopy) {
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
