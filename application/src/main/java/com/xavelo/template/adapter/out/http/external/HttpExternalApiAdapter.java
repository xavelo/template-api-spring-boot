package com.xavelo.template.adapter.out.http.external;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.application.domain.ExternalApiResult;
import com.xavelo.template.application.exception.ExternalApiUnavailableException;
import com.xavelo.template.application.port.out.CallExternalApiPort;
import com.xavelo.template.configuration.ExternalApiResilienceConfiguration;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

@Adapter
public class HttpExternalApiAdapter implements CallExternalApiPort {

    private static final String BASE_URL = "https://httpbin.org/status";

    private static final ConcurrentMap<Integer, ExternalApiResult> RESPONSE_CACHE = new ConcurrentHashMap<>();

    private final RestTemplate restTemplate;
    private final Retry retry;
    private final CircuitBreaker circuitBreaker;
    private final TimeLimiter timeLimiter;

    public HttpExternalApiAdapter(RestTemplateBuilder restTemplateBuilder,
                                  Retry retry,
                                  CircuitBreaker circuitBreaker,
                                  TimeLimiter timeLimiter) {
        this.restTemplate = restTemplateBuilder
                .errorHandler(noOpErrorHandler())
                .rootUri(BASE_URL)
                .build();
        this.retry = retry;
        this.circuitBreaker = circuitBreaker;
        this.timeLimiter = timeLimiter;
    }

    @Override
    @CountAdapterInvocation(
            name = "httpstatus-fetch",
            direction = AdapterMetrics.Direction.OUT,
            type = AdapterMetrics.Type.HTTP)
    @io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker(name = ExternalApiResilienceConfiguration.EXTERNAL_API_RESILIENCE_ID)
    @io.github.resilience4j.retry.annotation.Retry(name = ExternalApiResilienceConfiguration.EXTERNAL_API_RESILIENCE_ID,
            fallbackMethod = "callExternalApiFallback")
    public ExternalApiResult callExternalApi(int status) {
        Supplier<ExternalApiResult> supplier = () -> executeHttpCall(status);
        Supplier<ExternalApiResult> withRetry = io.github.resilience4j.retry.Retry.decorateSupplier(retry, supplier);
        Supplier<ExternalApiResult> resilientCall = io.github.resilience4j.circuitbreaker.CircuitBreaker.decorateSupplier(circuitBreaker, withRetry);

        try {
            ExternalApiResult result = timeLimiter.executeFutureSupplier(() ->
                    CompletableFuture.supplyAsync(resilientCall::get)
            );
            RESPONSE_CACHE.put(status, result);
            return result;
        } catch (Exception exception) {
            Throwable cause = exception.getCause() != null ? exception.getCause() : exception;
            return callExternalApiFallback(status, cause);
        }
    }

    @SuppressWarnings("unused")
    private ExternalApiResult callExternalApiFallback(int status, Throwable throwable) {
        ExternalApiResult cachedResult = RESPONSE_CACHE.get(status);
        if (cachedResult != null) {
            return cachedResult;
        }
        throw new ExternalApiUnavailableException(status, throwable);
    }

    private ExternalApiResult executeHttpCall(int status) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("/{status}", String.class, status);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RestClientException("httpbin.org returned non-success status: " + response.getStatusCode());
            }
            String body = response.getBody();
            if (body == null) {
                throw new RestClientException("httpbin.org returned an empty body");
            }
            int statusCode = response.getStatusCode().value();
            String requestUrl = BASE_URL + "/" + status;
            return new ExternalApiResult(String.valueOf(statusCode), statusCode, body, requestUrl);
        } catch (RestClientException e) {
            throw new RestClientException("Failed to fetch data from httpstat.us", e);
        }
    }

    void clearResponseCache() {
        RESPONSE_CACHE.clear();
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
