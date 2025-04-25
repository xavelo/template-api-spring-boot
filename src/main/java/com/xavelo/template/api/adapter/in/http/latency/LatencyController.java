package com.xavelo.template.api.adapter.in.http.latency;

import com.xavelo.template.port.in.SynchExpensiveOperationUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LatencyController {

    private static final Logger logger = LogManager.getLogger(LatencyController.class);

    private final SynchExpensiveOperationUseCase synchExpensiveOperationUseCase;

    public LatencyController(SynchExpensiveOperationUseCase synchExpensiveOperationUseCase) {
        this.synchExpensiveOperationUseCase = synchExpensiveOperationUseCase;
    }

    @GetMapping("/latency")
    public ResponseEntity<LatencyResponse> latencySync() {
        Long value = synchExpensiveOperationUseCase.blockingExpensiveOperation();
        LatencyResponse response = new LatencyResponse(value);
        return ResponseEntity.ok(response);
    }

}
