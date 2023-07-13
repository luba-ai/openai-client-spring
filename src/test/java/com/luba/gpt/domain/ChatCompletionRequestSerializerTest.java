package com.luba.gpt.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luba.gpt.parser.config.SchemaGeneratorConfig;
import com.luba.gpt.parser.service.GPTFunctionParser;
import java.util.List;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {GPTFunctionParser.class, SchemaGeneratorConfig.class})
class ChatCompletionRequestSerializerTest {

    @Test
    @SneakyThrows
    void serialize() {
        CompletionRequest request = new CompletionRequest();
        request.setModel("gpt-3.5-turbo-16k");
        request.setTemperature(0.9);
        CompletionMessage message = new CompletionMessage();
        message.setRole(Role.user);
        message.setContent("Hello world, how are you doing?!");
        request.setMessages(List.of(message));
        request.setFunctions(MyChatGPTFunctionClass.class);

        FunctionCall functionCall = new FunctionCall();
        functionCall.setName("parse_company");

        request.setFunctionCall(functionCall);
        ObjectMapper objectMapper = new ObjectMapper();

        String result = objectMapper.writeValueAsString(request);

        assertThat(result).isEqualToIgnoringWhitespace("""
            {
               "model":"gpt-3.5-turbo-16k",
               "temperature":0.9,
               "messages":[
                  {
                     "role":"user",
                     "content":"Hello world, how are you doing?!"
                  }
               ],
               "functions":[
                  {
                     "name":"parse_company",
                     "description":"Function that parses a company and returns it",
                     "parameters":{
                        "type":"object",
                        "properties":{
                           "location":{
                              "type":"string",
                              "description":null
                           },
                           "user":{
                              "type":"object",
                              "properties":{
                                 "name":{
                                    "type":"string",
                                    "description":"The name of the thing"
                                 },
                                 "id":{
                                    "type":"integer",
                                    "description":"The id of the thing"
                                 }
                              },
                              "required":[
                                 "name",
                                 "id"
                              ]
                           }
                        },
                        "required":[
                           "user"
                        ]
                     }
                  }
               ],
               "function_call": {
                    "name": "parse_company"
               }
            }
                        """);
    }


    private interface MyChatGPTFunctionClass extends IBaseFunction {

        @JsonPropertyDescription("Function that parses a company and returns it")
        TestCompany parse_company();

    }

    @Data
    class TestCompany {

        @JsonProperty(required = true)
        @JsonPropertyDescription("The user in the company")
        TestUser user;

        String location;
    }

    @Data
    class TestUser {

        @JsonProperty(required = true)
        @JsonPropertyDescription("The id of the thing")
        private Long id;

        @JsonProperty(required = true)
        @JsonPropertyDescription("The name of the thing")
        private String name;

    }
}
