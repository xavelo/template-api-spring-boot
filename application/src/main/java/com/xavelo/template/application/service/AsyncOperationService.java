package com.xavelo.template.application.service;

import com.xavelo.template.application.domain.AsyncOperationResult;
import com.xavelo.template.application.port.in.asyncoperation.AsyncOperationUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncOperationService implements AsyncOperationUseCase {

    private static final Logger logger = LogManager.getLogger(AsyncOperationService.class);

    private static final long SIMULATED_WORK_DURATION_MS = 750L;

    @Override
    @Async("applicationTaskExecutor")
    public CompletableFuture<AsyncOperationResult> triggerAsyncOperationAsync() {
        Instant start = Instant.now();
        try {
            Thread.sleep(SIMULATED_WORK_DURATION_MS);
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Async operation was interrupted", interruptedException);
        }
        long duration = Duration.between(start, Instant.now()).toMillis();
        AsyncOperationResult result = new AsyncOperationResult("Async operation completed", duration);
        logger.info("Async operation completed in {} ms", duration);
        return CompletableFuture.completedFuture(result);
    }
}
