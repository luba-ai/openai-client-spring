package com.luba.parser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaGeneratorConfig {

    @Bean
    public JsonSchemaGenerator schemaGenerator() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new JsonSchemaGenerator(objectMapper);
    }
}
