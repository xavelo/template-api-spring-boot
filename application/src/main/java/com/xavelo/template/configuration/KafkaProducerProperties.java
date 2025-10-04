package com.xavelo.template.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@ConfigurationProperties(prefix = "kafka")
public class KafkaProducerProperties {

    private final String bootstrapServers;

    public KafkaProducerProperties(String bootstrapServers) {
        this.bootstrapServers = Objects.requireNonNull(bootstrapServers, "bootstrapServers must not be null");
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }
}
