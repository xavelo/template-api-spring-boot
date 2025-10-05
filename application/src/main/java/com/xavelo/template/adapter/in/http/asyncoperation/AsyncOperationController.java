package com.xavelo.template.adapter.in.http.asyncoperation;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.api.contract.api.AsyncApi;
import com.xavelo.template.api.contract.model.AsyncOperationResponseDto;
import com.xavelo.template.application.domain.AsyncOperationResult;
import com.xavelo.template.application.port.in.asyncoperation.AsyncOperationUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Adapter
@RestController
public class AsyncOperationController implements AsyncApi {

    private static final Logger logger = LogManager.getLogger(AsyncOperationController.class);

    private final AsyncOperationUseCase asyncOperationUseCase;

    public AsyncOperationController(AsyncOperationUseCase asyncOperationUseCase) {
        this.asyncOperationUseCase = asyncOperationUseCase;
    }

    @Override
    @CountAdapterInvocation(
            name = "async-operation",
            direction = AdapterMetrics.Direction.IN,
            type = AdapterMetrics.Type.HTTP)
    public CompletableFuture<ResponseEntity<AsyncOperationResponseDto>> triggerAsyncOperation() {
        return asyncOperationUseCase.triggerAsyncOperationAsync()
                .thenApply(this::mapToResponse);
    }

    private ResponseEntity<AsyncOperationResponseDto> mapToResponse(AsyncOperationResult result) {
        logger.info("Returning result for async operation in {} ms", result.durationMs());
        AsyncOperationResponseDto responseDto = new AsyncOperationResponseDto()
                .message(result.message())
                .durationMs(result.durationMs());
        return ResponseEntity.ok(responseDto);
    }
}
