package com.xavelo.template.configuration;

import com.xavelo.template.application.exception.ExternalApiUnavailableException;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ExternalApiResilienceConfiguration {

    public static final String EXTERNAL_API_RESILIENCE_ID = "externalApi";

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> externalApiCircuitBreakerCustomizer() {
        return factory -> factory.configure(builder -> builder
                        .circuitBreakerConfig(circuitBreakerConfig())
                        .timeLimiterConfig(timeLimiterConfig())
                        .build(),
                EXTERNAL_API_RESILIENCE_ID);
    }

    private CircuitBreakerConfig circuitBreakerConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(2)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .recordExceptions(FeignException.class, ExternalApiUnavailableException.class)
                .build();
    }

    private TimeLimiterConfig timeLimiterConfig() {
        return TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(3))
                .build();
    }

}
