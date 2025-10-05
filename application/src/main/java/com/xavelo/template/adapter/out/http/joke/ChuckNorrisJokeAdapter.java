package com.xavelo.template.adapter.out.http.joke;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.application.domain.joke.Joke;
import com.xavelo.template.application.port.out.joke.GetRandomJokePort;
import com.xavelo.template.configuration.ChuckNorrisProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Adapter
public class ChuckNorrisJokeAdapter implements GetRandomJokePort {

    private final RestTemplate restTemplate;

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
    public Joke getRandomJoke() {
        try {
            ResponseEntity<ChuckNorrisJokeResponse> response = restTemplate.getForEntity("/jokes/random", ChuckNorrisJokeResponse.class);
            ChuckNorrisJokeResponse body = response.getBody();
            if (body == null) {
                throw new RestClientException("Chuck Norris API returned an empty body");
            }
            return new Joke(body.id(), body.value(), body.url());
        } catch (RestClientException e) {
            throw new RestClientException("Failed to fetch joke from Chuck Norris API", e);
        }
    }
}
