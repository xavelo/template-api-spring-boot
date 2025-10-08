package com.xavelo.template.adapter.out.http.external;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.adapter.out.http.external.client.ExternalApiClient;
import com.xavelo.template.application.domain.ExternalApiResult;
import com.xavelo.template.application.exception.ExternalApiUnavailableException;
import com.xavelo.template.application.port.out.CallExternalApiPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

@Adapter
public class HttpExternalApiAdapter implements CallExternalApiPort {

    private final ExternalApiClient externalApiClient;
    private final ExternalApiResponseCache responseCache;
    private final String baseUrl;

    public HttpExternalApiAdapter(ExternalApiClient externalApiClient,
                                  ExternalApiResponseCache responseCache,
                                  @Value("${external.api.base-url}") String baseUrl) {
        this.externalApiClient = externalApiClient;
        this.responseCache = responseCache;
        this.baseUrl = normalizeBaseUrl(baseUrl);
    }

    @Override
    @CountAdapterInvocation(
            name = "call-external-api",
            direction = AdapterMetrics.Direction.OUT,
            type = AdapterMetrics.Type.HTTP)
    public ExternalApiResult callExternalApi(int status) {
        try {
            ExternalApiResult result = executeHttpCall(status);
            responseCache.put(status, result);
            return result;
        } catch (ExternalApiUnavailableException exception) {
            return callExternalApiFallback(status, exception.getCause() != null ? exception.getCause() : exception);
        } catch (Exception exception) {
            return callExternalApiFallback(status, exception);
        }
    }

    @SuppressWarnings("unused")
    private ExternalApiResult callExternalApiFallback(int status, Throwable throwable) {
        return responseCache.get(status)
                .orElseThrow(() -> new ExternalApiUnavailableException(status, throwable));
    }

    private ExternalApiResult executeHttpCall(int status) {
        ResponseEntity<String> response = externalApiClient.fetchStatus(status);
        String body = response.getBody();
        if (body == null) body = "";
        int statusCode = response.getStatusCode().value();
        String requestUrl = buildRequestUrl(status);
        return new ExternalApiResult(String.valueOf(statusCode), statusCode, body, requestUrl);
    }

    private String buildRequestUrl(int status) {
        return String.format("%s/%d", baseUrl, status);
    }

    private static String normalizeBaseUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.isBlank()) {
            return "https://httpbin.org/status";
        }
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }
}
