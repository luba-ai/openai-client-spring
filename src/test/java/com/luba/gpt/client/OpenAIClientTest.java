package com.luba.gpt.client;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luba.gpt.domain.CompletionRequest;
import com.luba.gpt.domain.CompletionResponse;
import java.io.IOException;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

class OpenAIClientTest {

    private OpenAIClient underTest;

    private ObjectMapper objectMapper;

    private MockWebServer mockWebServer;


    @BeforeEach
    void start() throws IOException {
        objectMapper = new ObjectMapper();
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        WebClient webClient = WebClient.builder().baseUrl(mockWebServer.url("/").toString()).build();
        underTest = new OpenAIClient(webClient);
    }

    @AfterEach
    void shutdown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @SneakyThrows
    void create() {
        CompletionRequest completionRequest = new CompletionRequest();
        CompletionResponse completionResponse = new CompletionResponse();
        MockResponse response = new MockResponse()
            .setBody(objectMapper.writeValueAsString(completionResponse))
            .addHeader("Content-Type", "application/json");
        mockWebServer.enqueue(response);


        CompletionResponse result = underTest.create(completionRequest);

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath()).isEqualTo("/chat/completions");
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getHeader("Content-Type")).isEqualTo("application/json");
        assertThat(result).isNotNull();
    }

}
