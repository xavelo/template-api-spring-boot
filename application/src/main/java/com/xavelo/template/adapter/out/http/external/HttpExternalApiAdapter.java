package com.xavelo.template.adapter.out.http.external;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.application.domain.ExternalApiResult;
import com.xavelo.template.application.port.out.CallExternalApiPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Adapter
public class HttpExternalApiAdapter implements CallExternalApiPort {

    private static final String BASE_URL = "https://httpstat.us";

    private final RestTemplate restTemplate;

    public HttpExternalApiAdapter(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(noOpErrorHandler())
                .rootUri(BASE_URL)
                .build();
    }

    @Override
    @CountAdapterInvocation(
            name = "httpstatus-fetch",
            direction = AdapterMetrics.Direction.OUT,
            type = AdapterMetrics.Type.HTTP)
    public ExternalApiResult callExternalApi(int status) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("/{status}", String.class, status);
            String body = response.getBody();
            if (body == null) {
                throw new RestClientException("httpstat.us returned an empty body");
            }
            int statusCode = response.getStatusCode().value();
            String requestUrl = BASE_URL + "/" + status;
            return new ExternalApiResult(String.valueOf(statusCode), statusCode, body, requestUrl);
        } catch (RestClientException e) {
            throw new RestClientException("Failed to fetch data from httpstat.us", e);
        }
    }

    private static ResponseErrorHandler noOpErrorHandler() {
        return new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
        };
    }
}
