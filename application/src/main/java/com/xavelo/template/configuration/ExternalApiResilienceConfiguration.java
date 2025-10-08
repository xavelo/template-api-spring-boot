package com.xavelo.template.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExternalApiResilienceConfiguration {

    public static final String EXTERNAL_API_RESILIENCE_ID = "externalApi";

    @Bean
    public CircuitBreaker externalApiCircuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry) {
        return circuitBreakerRegistry.circuitBreaker(EXTERNAL_API_RESILIENCE_ID);
    }

    @Bean
    public Retry externalApiRetry(RetryRegistry retryRegistry) {
        return retryRegistry.retry(EXTERNAL_API_RESILIENCE_ID);
    }

    @Bean
    public TimeLimiter externalApiTimeLimiter(TimeLimiterRegistry timeLimiterRegistry) {
        return timeLimiterRegistry.timeLimiter(EXTERNAL_API_RESILIENCE_ID);
    }
}
