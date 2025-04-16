package com.xavelo.template.api.adapter.in.http;

import lombok.Getter;

@Getter
public class LatencyResponse {
    private Long value;

    public LatencyResponse(Long value) {
        this.value = value;
    }

    public LatencyResponse() {
        // Default constructor
    }
}
