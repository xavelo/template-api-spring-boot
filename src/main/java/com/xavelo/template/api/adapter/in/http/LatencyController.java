package com.xavelo.template.api.adapter.in.http;

import com.xavelo.template.api.application.service.LatencyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class LatencyController {

    private static final Logger logger = LogManager.getLogger(LatencyController.class);

    private final LatencyService latencyService;

    public LatencyController(LatencyService latencyService) {
        this.latencyService = latencyService;
    }

    @GetMapping("/latency-asynch")
    public Mono<ResponseEntity<LatencyResponse>> latencyAsynch() {
        return latencyService.getLatencyAsynch()
            .map(latency -> {
                LatencyResponse response = new LatencyResponse(latency.longValue());
                return ResponseEntity.ok(response);
            });
    }

    @GetMapping("/latency-synch")
    public ResponseEntity<LatencyResponse> latencySync() {
        Long value = latencyService.getLatency();
        LatencyResponse response = new LatencyResponse(value);
        return ResponseEntity.ok(response);
    }

}
