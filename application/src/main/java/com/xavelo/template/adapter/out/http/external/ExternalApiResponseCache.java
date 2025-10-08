package com.xavelo.template.adapter.out.http.external;

import com.xavelo.template.application.domain.ExternalApiResult;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class ExternalApiResponseCache {

    private final ConcurrentMap<Integer, ExternalApiResult> cache = new ConcurrentHashMap<>();

    public void put(int status, ExternalApiResult result) {
        cache.put(status, result);
    }

    public Optional<ExternalApiResult> get(int status) {
        return Optional.ofNullable(cache.get(status));
    }

    public void clear() {
        cache.clear();
    }
}
