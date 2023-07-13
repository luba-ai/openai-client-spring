# ChatGPT Spring Boot Library

The ChatGPT Spring Boot Library is a handy tool for Java developers looking to integrate the OpenAI's ChatGPT API into their Spring Boot applications. This library handles all the nitty-gritty of establishing the connection, handling responses, and managing errors, thus allowing developers to focus on the core business logic.

## Prerequisites

Before using this library, you need:

- Java Development Kit (JDK) 1.8 or higher
- Spring Boot 2.3.1 or higher
- A valid OpenAI API Key

## Installation

1. Download the JAR file from the Releases page or use the dependency code below to add this library to your Maven or Gradle project.

For Maven:

```xml
<dependency>
    <groupId>com.luba</groupId>
    <artifactId>spring-openai-client</artifactId>
    <version>${luba-openai-client.version}</version>
</dependency>
```

For Gradle:

```groovy
implementation 'com.luba:spring-openai-client:0.0.2'
```

## Configuration

Prior to using the ChatGPT API, you should configure your API Keys. It's recommended to store them as environment variables for security reasons:

```bash
export OPENAI_API_KEY=your-openai-api-key
```

The application will then automatically pick up the API key from the environment variable.

If you prefer, you can directly set your API key in the properties file, but take care in not exposing sensitive data:

```props
luba:
  openai:
    key: ${OPENAI_API_KEY}
```


You can change the default configuration of the client by setting the following properties:

```props
export OPENAI_MODEL=gpt-3.5-turbo
export OPENAI_TEMPERATURE=0.6
```



## Usage

This library provides an easy-to-use `GPTService` class. Here's an example of how to use this class to send a message to the ChatGPT API:

### Basic Completion

```java
import com.luba.gpt.domain.CompletionMessage;
import com.luba.gpt.domain.CompletionResponse;
import com.luba.gpt.domain.Role;
import com.luba.gpt.service.GPTService;
import org.springframework.beans.factory.annotation.Autowired;

public class ExampleClass {

    @Autowired
    private GPTService gptService;

    public void completionWithCustomConfiguration() {
        String prompt = "Translate the following English text to French: '{\"text\": \"Hello, world!\"}'";
        CompletionMessage message = CompletionMessage
            .builder()
            .content(prompt)
            .role(Role.user)
            .build();
        List<CompletionMessage> messages = List.of(message);
        CompletionResponse result = gptService.completion(messages);
        System.out.println(result);
    }
}
```

In the above example, `prompt` is the input message, `role` is the role in the message, and `result` is the model's output.

### Advanced Completion

One can also override the default configuration of the client by passing the full request object with the desired configuration:

```java
import com.luba.gpt.domain.CompletionMessage;
import com.luba.gpt.domain.CompletionRequest;
import com.luba.gpt.domain.CompletionResponse;
import com.luba.gpt.domain.Role;
import com.luba.gpt.service.GPTService;
import org.springframework.beans.factory.annotation.Autowired;

public class ExampleClass {

    @Autowired
    private GPTService gptService;

    public void completionWithCustomConfiguration() {
        String prompt = "Translate the following English text to French: '{\"text\": \"Hello, world!\"}'";
        CompletionMessage message = CompletionMessage
            .builder()
            .content(prompt)
            .role(Role.user)
            .build();
        List<CompletionMessage> messages = List.of(message);

        CompletionRequest request = new CompletionRequest();
        request.setEngine("gpt-3.5-turbo");
        request.setTemperature(0.6);
        request.setMessages(messages);

        CompletionResponse result = gptService.completion(request);
        System.out.println(result);
    }
}
```


### Functions

With the introduction of functions to the ChatGPT API, the library gives a programmatic way of writing functions and translating them into ChatGPT functions. Here's an example of how to use the `GPTService` class to send a message with a function to the ChatGPT API:

```java

import com.luba.gpt.domain.CompletionMessage;
import com.luba.gpt.domain.CompletionRequest;
import com.luba.gpt.domain.CompletionResponse;
import com.luba.gpt.domain.Role;
import com.luba.gpt.service.GPTService;
import org.springframework.beans.factory.annotation.Autowired;

public class ExampleClass {

    @Autowired
    private GPTService gptService;

    public void completionWithCustomConfiguration() {
        String prompt = "Translate the following English text to French: '{\"text\": \"Hello, world!\"}'";
        CompletionMessage message = CompletionMessage
            .builder()
            .content(prompt)
            .role(Role.user)
            .build();
        List<CompletionMessage> messages = List.of(message);

        CompletionRequest request = new CompletionRequest();
        request.setEngine("gpt-3.5-turbo");
        request.setTemperature(0.6);
        request.setMessages(messages);
        request.setFunctions(MyChatGPTFunctionClass.class);

        CompletionResponse result = gptService.completion(request);
        System.out.println(result);
    }
}

interface MyChatGPTFunctionClass extends IBaseFunction {

    @JsonPropertyDescription("Function that parses a user and returns it")
    User parse_user();

    @JsonPropertyDescription("Function that extracts all books in a specific message")
    Recommendations extract_books();
}

@Data
class User {

    @JsonProperty(required = true)
    @JsonPropertyDescription("The name of the user provided in the message")
    String name;

    @JsonPropertyDescription("The location of the user sending the message")
    String location;

    @JsonPropertyDescription("The creation date of this message")
    String createdDate;
}
```

The library will automatically convert all methods in the provided class to ChatGPT functions and ensure proper serialization.

One can specify descriptions of each method and object by using the `@JsonPropertyDescription` annotation. This will ensure that the ChatGPT API will have the proper descriptions for each function and object.
One can additionally specify the `@JsonProperty(required = true)` annotation to specify that a member of the object is required.

```java

## License

This project is licensed under [MIT License](LICENSE.md).
