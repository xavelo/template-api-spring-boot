package com.xavelo.template.adapter.out.joke;

import com.xavelo.template.configuration.ChuckNorrisProperties;
import com.xavelo.template.joke.Joke;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ChuckNorrisJokeClient implements JokeClient {

    private final RestTemplate restTemplate;

    public ChuckNorrisJokeClient(RestTemplateBuilder restTemplateBuilder, ChuckNorrisProperties properties) {
        this.restTemplate = restTemplateBuilder
                .rootUri(properties.getBaseUrl())
                .build();
    }

    @Override
    public Joke fetchRandomJoke() {
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
