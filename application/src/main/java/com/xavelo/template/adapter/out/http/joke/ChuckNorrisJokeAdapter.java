package com.xavelo.template.adapter.out.http.joke;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.application.domain.Joke;
import com.xavelo.template.application.exception.JokeUnavailableException;
import com.xavelo.template.application.port.out.GetRandomJokePort;
import com.xavelo.template.configuration.ChuckNorrisProperties;
import com.xavelo.template.configuration.ChuckNorrisResilienceConfiguration;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicReference;

@Adapter
public class ChuckNorrisJokeAdapter implements GetRandomJokePort {

    private final RestTemplate restTemplate;
    private final AtomicReference<Joke> lastSuccessfulJoke = new AtomicReference<>();

    public ChuckNorrisJokeAdapter(RestTemplateBuilder restTemplateBuilder, ChuckNorrisProperties properties) {
        this.restTemplate = restTemplateBuilder
                .rootUri(properties.getBaseUrl())
                .build();
    }

    @Override
    @CountAdapterInvocation(
            name = "chuck-norris-random",
            direction = AdapterMetrics.Direction.OUT,
            type = AdapterMetrics.Type.HTTP)
    @Retry(name = ChuckNorrisResilienceConfiguration.CHUCK_NORRIS_RANDOM)
    @CircuitBreaker(
            name = ChuckNorrisResilienceConfiguration.CHUCK_NORRIS_RANDOM,
            fallbackMethod = "getRandomJokeFallback")
    public Joke getRandomJoke() {
        try {
            ResponseEntity<ChuckNorrisJokeResponse> response = restTemplate.getForEntity("/jokes/random", ChuckNorrisJokeResponse.class);
            ChuckNorrisJokeResponse body = response.getBody();
            if (body == null) {
                throw new RestClientException("Chuck Norris API returned an empty body");
            }
            Joke joke = new Joke(body.id(), body.value(), body.url());
            lastSuccessfulJoke.set(joke);
            return joke;
        } catch (RestClientException e) {
            throw new RestClientException("Failed to fetch joke from Chuck Norris API", e);
        }
    }

    @SuppressWarnings("unused")
    private Joke getRandomJokeFallback(Throwable throwable) {
        Joke cachedJoke = lastSuccessfulJoke.get();
        if (cachedJoke != null) {
            return cachedJoke;
        }
        throw new JokeUnavailableException("Chuck Norris jokes are temporarily unavailable", throwable);
    }
}
