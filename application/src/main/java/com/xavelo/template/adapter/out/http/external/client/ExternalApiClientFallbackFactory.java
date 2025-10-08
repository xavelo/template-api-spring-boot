package com.xavelo.template.adapter.out.http.external.client;

import com.xavelo.template.adapter.out.http.external.ExternalApiResponseCache;
import com.xavelo.template.application.domain.ExternalApiResult;
import com.xavelo.template.application.exception.ExternalApiUnavailableException;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ExternalApiClientFallbackFactory implements FallbackFactory<ExternalApiClient> {

    private final ExternalApiResponseCache responseCache;

    ExternalApiClientFallbackFactory(ExternalApiResponseCache responseCache) {
        this.responseCache = responseCache;
    }

    @Override
    public ExternalApiClient create(Throwable cause) {
        return status -> responseCache.get(status)
                .map(result -> toResponseEntity(result))
                .orElseThrow(() -> new ExternalApiUnavailableException(status, cause));
    }

    private ResponseEntity<String> toResponseEntity(ExternalApiResult result) {
        HttpStatus httpStatus = HttpStatus.resolve(result.status());
        if (httpStatus == null) {
            httpStatus = HttpStatus.OK;
        }
        return ResponseEntity.status(httpStatus).body(result.value());
    }
}
