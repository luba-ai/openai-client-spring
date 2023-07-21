package com.luba.parser.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.luba.domain.IBaseFunction;
import com.luba.parser.config.SchemaGeneratorConfig;
import com.luba.parser.data.ArrayType;
import com.luba.parser.data.Function;
import com.luba.parser.data.ObjectType;
import com.luba.parser.data.SimpleType;
import java.util.HashMap;
import java.util.Set;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {GPTFunctionParser.class, SchemaGeneratorConfig.class})
class GPTFunctionParserTest {

    @Autowired
    private GPTFunctionParser underTest;

    @Test
    @SneakyThrows
    void extractFunctions() {

        Function[] result = underTest.extractFunctions(MyChatGPTFunctionClass.class);

        assertThat(result).isNotNull().hasSize(2);
        Function firstFunction = new Function();
        firstFunction.setName("parse_user");
        firstFunction.setDescription("Function that parses a user and returns it");
        ObjectType parameters = new ObjectType();
        parameters.setProperties(new HashMap<>() {{
            put("name", new SimpleType("string", "The name of the user provided in the message"));
            put("location", new SimpleType("string", "The location of the user sending the message"));
            put("createdDate", new SimpleType("string", "The creation date of this message"));
        }});
        parameters.setRequired(Set.of("name"));
        firstFunction.setParameters(parameters);

        Function secondFunction = new Function();
        secondFunction.setName("extract_books");
        secondFunction.setDescription("Function that extracts all books in a specific message");
        ObjectType secondParameters = new ObjectType();
        secondParameters.setProperties(new HashMap<>() {{
            put("books", new ArrayType("array", new ObjectType() {{
                setProperties(new HashMap<>() {{
                    put("isbn", new SimpleType("string", "The ISBN of the book"));
                    put("title", new SimpleType("string", "The title of the book"));
                    put("author", new SimpleType("string", "The author of the book"));
                }});
                setRequired(Set.of("author", "isbn", "title"));
            }}));
        }});
        secondParameters.setRequired(Set.of("books"));
        secondFunction.setParameters(secondParameters);
        assertThat(result).isNotNull().contains(firstFunction, secondFunction);
    }

    private interface MyChatGPTFunctionClass extends IBaseFunction {

        @JsonPropertyDescription("Function that parses a user and returns it")
        User parse_user();

        @JsonPropertyDescription("Function that extracts all books in a specific message")
        Recommendations extract_books();
    }

    @Data
    public class User {

        @JsonProperty(required = true)
        @JsonPropertyDescription("The name of the user provided in the message")
        String name;

        @JsonPropertyDescription("The location of the user sending the message")
        String location;

        @JsonPropertyDescription("The creation date of this message")
        String createdDate;
    }

    public class Recommendations {

        @JsonProperty(required = true)
        @JsonPropertyDescription("The books in the message")
        private RecommendationBase[] books;
    }

    private static class RecommendationBase {

        @JsonProperty(required = true)
        @JsonPropertyDescription("The ISBN of the book")
        private String isbn;

        @JsonProperty(required = true)
        @JsonPropertyDescription("The title of the book")
        private String title;

        @JsonProperty(required = true)
        @JsonPropertyDescription("The author of the book")
        private String author;
    }

}
