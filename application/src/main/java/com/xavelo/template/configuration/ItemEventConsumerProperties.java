package com.xavelo.template.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "template.kafka.item-events")
public class ItemEventConsumerProperties {

    private String topic = "test-topic";
    private String groupId = "template-item-event-consumer";
    private int concurrency = 1;
    private long errorBackoffIntervalMs = 1000L;
    private long errorMaxRetries = 3L;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency < 1 ? 1 : concurrency;
    }

    public long getErrorBackoffIntervalMs() {
        return errorBackoffIntervalMs;
    }

    public void setErrorBackoffIntervalMs(long errorBackoffIntervalMs) {
        this.errorBackoffIntervalMs = errorBackoffIntervalMs < 0 ? 0 : errorBackoffIntervalMs;
    }

    public long getErrorMaxRetries() {
        return errorMaxRetries;
    }

    public void setErrorMaxRetries(long errorMaxRetries) {
        this.errorMaxRetries = errorMaxRetries < 0 ? 0 : errorMaxRetries;
    }
}
