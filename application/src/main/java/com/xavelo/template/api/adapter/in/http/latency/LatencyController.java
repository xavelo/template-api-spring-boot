package com.xavelo.template.api.adapter.in.http.latency;

import com.xavelo.api.dto.LatencyResponse;
import com.xavelo.api.interfaces.LatencyApi;
import com.xavelo.template.port.in.SynchExpensiveOperationUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LatencyController implements LatencyApi {

    private static final Logger logger = LogManager.getLogger(LatencyController.class);

    private final SynchExpensiveOperationUseCase synchExpensiveOperationUseCase;

    public LatencyController(SynchExpensiveOperationUseCase synchExpensiveOperationUseCase) {
        this.synchExpensiveOperationUseCase = synchExpensiveOperationUseCase;
    }

    @Override
    public ResponseEntity<LatencyResponse> getLatency() {
        Long value = synchExpensiveOperationUseCase.blockingExpensiveOperation();
        logger.info("Latency endpoint executed expensive operation with result: {}", value);
        LatencyResponse response = new LatencyResponse().value(value);
        return ResponseEntity.ok(response);
    }
}
