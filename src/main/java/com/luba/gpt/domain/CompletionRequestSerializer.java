package com.luba.gpt.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.luba.gpt.parser.service.GPTFunctionParser;
import java.io.IOException;

class CompletionRequestSerializer extends StdSerializer<CompletionRequest> {

    private final GPTFunctionParser functionParser;

    protected CompletionRequestSerializer(Class<CompletionRequest> t) {
        super(t);
        this.functionParser = GPTFunctionParser.get();
    }

    public CompletionRequestSerializer() {
        this(null);
    }

    @Override
    public void serialize(CompletionRequest chatCompletionRequest, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("model", chatCompletionRequest.getModel());
        jsonGenerator.writeNumberField("temperature", chatCompletionRequest.getTemperature());
        jsonGenerator.writeArrayFieldStart("messages");
        if (chatCompletionRequest.getMessages() != null && !chatCompletionRequest.getMessages().isEmpty()) {
            for (CompletionMessage message : chatCompletionRequest.getMessages()) {
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
