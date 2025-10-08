package com.xavelo.template.adapter.out.http.external.client;

import com.xavelo.template.configuration.ExternalApiResilienceConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = ExternalApiResilienceConfiguration.EXTERNAL_API_RESILIENCE_ID,
        url = "${external.api.base-url}",
        fallbackFactory = ExternalApiClientFallbackFactory.class
)
public interface ExternalApiClient {

    @GetMapping("/{status}")
    ResponseEntity<String> fetchStatus(@PathVariable("status") int status);
}
