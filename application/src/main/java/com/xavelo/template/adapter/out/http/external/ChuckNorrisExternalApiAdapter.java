package com.xavelo.template.adapter.out.http.external;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.application.domain.ExternalApiResult;
import com.xavelo.template.application.port.out.CallExternalApiPort;
import com.xavelo.template.configuration.ChuckNorrisProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Adapter
public class ChuckNorrisExternalApiAdapter implements CallExternalApiPort {

    private final RestTemplate restTemplate;

    public ChuckNorrisExternalApiAdapter(RestTemplateBuilder restTemplateBuilder, ChuckNorrisProperties properties) {
        this.restTemplate = restTemplateBuilder
                .rootUri(properties.getBaseUrl())
                .build();
    }

    @Override
    @CountAdapterInvocation(
            name = "chuck-norris-random",
            direction = AdapterMetrics.Direction.OUT,
            type = AdapterMetrics.Type.HTTP)
    public ExternalApiResult callExternalApi() {
        try {
            ResponseEntity<ChuckNorrisExternalApiResponse> response = restTemplate.getForEntity(
                    "/jokes/random",
                    ChuckNorrisExternalApiResponse.class);
            ChuckNorrisExternalApiResponse body = response.getBody();
            if (body == null) {
                throw new RestClientException("Chuck Norris API returned an empty body");
            }
            return new ExternalApiResult(body.id(), body.value(), body.url());
        } catch (RestClientException e) {
            throw new RestClientException("Failed to fetch data from the Chuck Norris API", e);
        }
    }
}
