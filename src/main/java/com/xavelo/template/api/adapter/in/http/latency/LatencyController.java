package com.xavelo.template.api.adapter.in.http.latency;

import com.xavelo.template.port.in.AsynchExpensiveOperationUseCase;
import com.xavelo.template.port.in.SynchExpensiveOperationUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class LatencyController {

    private static final Logger logger = LogManager.getLogger(LatencyController.class);

    private final AsynchExpensiveOperationUseCase asynchExpensiveOperationUseCase;
    private final SynchExpensiveOperationUseCase synchExpensiveOperationUseCase;

    public LatencyController(AsynchExpensiveOperationUseCase asynchExpensiveOperationUseCase, SynchExpensiveOperationUseCase synchExpensiveOperationUseCase) {
        this.asynchExpensiveOperationUseCase = asynchExpensiveOperationUseCase;
        this.synchExpensiveOperationUseCase = synchExpensiveOperationUseCase;
    }

    @GetMapping("/latency/asynch")
    public Mono<ResponseEntity<LatencyResponse>> latencyAsynch() {
        return asynchExpensiveOperationUseCase.nonBlockingExpensiveOperation()
            .map(latency -> {
                LatencyResponse response = new LatencyResponse(latency.longValue());
                return ResponseEntity.ok(response);
            });
    }

    @GetMapping("/latency/synch")
    public ResponseEntity<LatencyResponse> latencySync() {
        Long value = synchExpensiveOperationUseCase.blockingExpensiveOperation();
        LatencyResponse response = new LatencyResponse(value);
        return ResponseEntity.ok(response);
    }

}
