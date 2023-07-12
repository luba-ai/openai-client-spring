package com.luba.gpt.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.luba.gpt.parser.service.GPTFunctionParser;
import java.io.IOException;

class ChatCompletionRequestSerializer extends StdSerializer<ChatCompletionRequest> {

    private final GPTFunctionParser functionParser;

    protected ChatCompletionRequestSerializer(Class<ChatCompletionRequest> t) {
        super(t);
        this.functionParser = GPTFunctionParser.get();
    }

    public ChatCompletionRequestSerializer() {
        this(null);
    }

    @Override
    public void serialize(ChatCompletionRequest chatCompletionRequest, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
        throws IOException {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("model", chatCompletionRequest.getModel());
        jsonGenerator.writeNumberField("temperature", chatCompletionRequest.getTemperature());
        jsonGenerator.writeArrayFieldStart("messages");
        if (chatCompletionRequest.getMessages() != null && !chatCompletionRequest.getMessages().isEmpty()) {
            for (ChatCompletionMessage message : chatCompletionRequest.getMessages()) {
                jsonGenerator.writeObject(message);
            }
        }
        jsonGenerator.writeEndArray();

        if (chatCompletionRequest.getFunctions() != null) {
            jsonGenerator.writeObjectField("functions", this.functionParser.extractFunctions(chatCompletionRequest.getFunctions()));
        }
        jsonGenerator.writeEndObject();
    }
}
