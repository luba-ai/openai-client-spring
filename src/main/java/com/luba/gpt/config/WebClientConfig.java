package com.luba.gpt.config;


import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
class WebClientConfig {

    private final OpenAIConfig config;

    @Bean
    public WebClient openAiWebClient() {
        HttpClient httpClient = new HttpClient() {
            @Override
            public Request newRequest(URI uri) {
                Request request = super.newRequest(uri);
                return enhance(request);
            }
        };
        return WebClient
            .builder()
            .baseUrl(config.getUrl())
            .clientConnector(new JettyClientHttpConnector(httpClient))
            .defaultHeader("Authorization", "Bearer " + config.getKey())
            .build();
    }


    private Request enhance(Request inboundRequest) {
        StringBuilder log = new StringBuilder();
        // Request Logging
        inboundRequest.onRequestBegin(request ->
            log.append("Request: \n")
                .append("URI: ")
                .append(request.getURI())
                .append("\n")
                .append("Method: ")
                .append(request.getMethod()));
        inboundRequest.onRequestHeaders(request -> {
            log.append("\nHeaders:\n");
            for (HttpField header : request.getHeaders()) {
                log.append("\t\t" + header.getName() + " : " + header.getValue() + "\n");
            }
        });
        inboundRequest.onRequestContent((request, content) ->
            log.append("Body: \n\t")
                .append(byteBufferToString(content, Charset.defaultCharset())));
        log.append("\n");

        // Response Logging
        inboundRequest.onResponseBegin(response ->
            log.append("Response:\n")
                .append("Status: ")
                .append(response.getStatus())
                .append("\n"));
        inboundRequest.onResponseHeaders(response -> {
            log.append("Headers:\n");
            for (HttpField header : response.getHeaders()) {
                log.append("\t\t" + header.getName() + " : " + header.getValue() + "\n");
            }
        });
        inboundRequest.onResponseContent(((response, content) -> {
            var bufferAsString = StandardCharsets.UTF_8.decode(content).toString();
            log.append("Response Body:\n" + bufferAsString);
        }));

        // Add actual log invocation
        System.out.println("HTTP ->\n");
        inboundRequest.onRequestSuccess(request -> System.out.println(log));
        inboundRequest.onResponseSuccess(request -> System.out.println(log));

        // Return original request
        return inboundRequest;
    }

    private static String byteBufferToString(ByteBuffer buffer, Charset charset) {
        byte[] bytes;
        if (buffer.hasArray()) {
            bytes = buffer.array();
        } else {
            buffer.rewind();
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
        }
        return new String(bytes, charset);
    }
}
