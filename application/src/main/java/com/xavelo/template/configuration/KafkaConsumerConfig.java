package com.xavelo.template.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(ItemEventConsumerProperties.class)
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, String> itemEventConsumerFactory(
        KafkaProperties kafkaProperties,
        ItemEventConsumerProperties itemEventConsumerProperties
    ) {
        Map<String, Object> consumerProperties = new HashMap<>(kafkaProperties.buildConsumerProperties());
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, itemEventConsumerProperties.getGroupId());
        return new DefaultKafkaConsumerFactory<>(consumerProperties);
    }

    @Bean
    public DefaultErrorHandler itemEventErrorHandler(ItemEventConsumerProperties itemEventConsumerProperties) {
        FixedBackOff backOff = new FixedBackOff(
            itemEventConsumerProperties.getErrorBackoffIntervalMs(),
            itemEventConsumerProperties.getErrorMaxRetries()
        );
        return new DefaultErrorHandler(backOff);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> itemEventKafkaListenerContainerFactory(
        ConsumerFactory<String, String> itemEventConsumerFactory,
        DefaultErrorHandler itemEventErrorHandler,
        ItemEventConsumerProperties itemEventConsumerProperties
    ) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(itemEventConsumerFactory);
        factory.setConcurrency(itemEventConsumerProperties.getConcurrency());
        factory.setCommonErrorHandler(itemEventErrorHandler);
        return factory;
    }
}
