package com.luba.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "luba.openai")
public class OpenAIConfig {

    private String key;
    private String url;
    private String model;
    private Double temperature;
    private Integer tokenLimit;
}
