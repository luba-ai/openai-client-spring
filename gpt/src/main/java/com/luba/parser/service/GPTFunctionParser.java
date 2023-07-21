package com.luba.parser.service;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.luba.domain.IBaseFunction;
import com.luba.parser.data.ArrayType;
import com.luba.parser.data.BaseType;
import com.luba.parser.data.Function;
import com.luba.parser.data.ObjectType;
import com.luba.parser.data.SimpleType;
import jakarta.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GPTFunctionParser {

    private static GPTFunctionParser instance;

    private final JsonSchemaGenerator schemaGenerator;

    @PostConstruct
    void init() {
        instance = this;
    }

    public static GPTFunctionParser get() {
        return instance;
    }

    @SneakyThrows
    public Function[] extractFunctions(Class<? extends IBaseFunction> cls) {
        Method[] methods = cls.getMethods();
        return Arrays.stream(methods).map((method) -> {
            Function function = new Function();
            function.setName(method.getName());
            Optional.ofNullable(method.getAnnotation(JsonPropertyDescription.class))
                .map(JsonPropertyDescription::value)
                .ifPresent(function::setDescription);
            function.setParameters(parseParameter(method.getReturnType()));
            return function;
        }).toArray(Function[]::new);
    }

    @SneakyThrows
    private BaseType parseParameter(Class<?> cls) {
        JsonSchema jsonSchema = schemaGenerator.generateSchema(cls);
        return parseTopSchema(jsonSchema);
    }

    private BaseType parseTopSchema(JsonSchema jsonSchema) {
        return parse(jsonSchema, null);
    }

    private ArrayType parseArray(JsonSchema schema) {
        ArrayType arrayType = new ArrayType();
        JsonSchema jsonSchema = schema.asArraySchema().getItems().asSingleItems().getSchema();
        arrayType.setItems(parse(jsonSchema, null));
        return arrayType;
    }

    private BaseType parseObject(JsonSchema schema) {
        ObjectType object = new ObjectType();
        Set<String> requiredFields = new HashSet<>();
        Map<String, BaseType> nameToType = new HashMap<>();

        schema.asObjectSchema().getProperties().forEach((key, value) -> {
            if (Boolean.TRUE.equals(value.getRequired())) {
                requiredFields.add(key);
            }
            nameToType.put(key, parse(value, value.getDescription()));
        });

        object.setProperties(nameToType);
        object.setRequired(requiredFields);
        return object;
    }

    private BaseType parse(JsonSchema schema, String description) {
        if (!schema.isObjectSchema() && !schema.isArraySchema()) {

            String type = schema.getType().value();
            SimpleType object = new SimpleType();
            object.setType(type);
            object.setDescription(description);
            return object;
        }

        if (schema.isArraySchema()) {
            return parseArray(schema);
        }

        return parseObject(schema);
    }

}
