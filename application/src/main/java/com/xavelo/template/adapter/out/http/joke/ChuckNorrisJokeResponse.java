package com.xavelo.template.adapter.out.http.joke;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChuckNorrisJokeResponse(
        String id,
        @JsonProperty("value") String value,
        String url
) {
}
