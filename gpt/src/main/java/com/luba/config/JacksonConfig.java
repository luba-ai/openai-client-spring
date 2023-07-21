package com.luba.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.luba.domain.CompletionRequest;
import com.luba.domain.CompletionRequestSerializer;
import com.luba.parser.service.GPTFunctionParser;
import com.luba.service.TokenizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class JacksonConfig {

    @Bean
    public SimpleModule completionRequestSerializer(OpenAIConfig config, TokenizationService tokenizationService, GPTFunctionParser functionParser) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(CompletionRequest.class, new CompletionRequestSerializer(functionParser, tokenizationService, config));
        return module;
    }
}
