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
public class ChuckNorrisResilienceConfiguration {

    public static final String CHUCK_NORRIS_RANDOM = "chuck-norris-random";

    @Bean
    public CircuitBreaker chuckNorrisRandomCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker(CHUCK_NORRIS_RANDOM);
    }

    @Bean
    public Retry chuckNorrisRandomRetry(RetryRegistry registry) {
        return registry.retry(CHUCK_NORRIS_RANDOM);
    }

    @Bean
    public TimeLimiter chuckNorrisRandomTimeLimiter(TimeLimiterRegistry registry) {
        return registry.timeLimiter(CHUCK_NORRIS_RANDOM);
    }
}
