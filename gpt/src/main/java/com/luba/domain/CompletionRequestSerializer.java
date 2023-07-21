package com.luba.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.luba.config.OpenAIConfig;
import com.luba.parser.service.GPTFunctionParser;
import com.luba.service.TokenizationService;
import java.io.IOException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

class CompletionRequestSerializer extends JsonSerializer<CompletionRequest> {

    @Autowired
    GPTFunctionParser functionParser;

    @Autowired
    TokenizationService tokenizationService;

    @Autowired
    OpenAIConfig config;

    public CompletionRequestSerializer(){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void serialize(CompletionRequest chatCompletionRequest, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("model", chatCompletionRequest.getModel());
        jsonGenerator.writeNumberField("temperature", chatCompletionRequest.getTemperature());
        jsonGenerator.writeArrayFieldStart("messages");

        Integer tokenCount = 0;
        if (chatCompletionRequest.getMessages() != null && !chatCompletionRequest.getMessages().isEmpty()) {

            Collections.reverse(chatCompletionRequest.getMessages());

            for (CompletionMessage message : chatCompletionRequest.getMessages()) {
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
