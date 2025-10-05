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
import java.util.concurrent.ThreadLocalRandom;

@Service
public class AsyncOperationService implements AsyncOperationUseCase {

    private static final Logger logger = LogManager.getLogger(AsyncOperationService.class);

    private static final long MIN_SIMULATED_WORK_DURATION_MS = 500L;
    private static final long MAX_SIMULATED_WORK_DURATION_MS = 2000L;
    private static final int MIN_WORK_COMPLEXITY = 150_000;
    private static final int MAX_WORK_COMPLEXITY = 600_000;

    @Override
    @Async("applicationTaskExecutor")
    public CompletableFuture<AsyncOperationResult> triggerAsyncOperationAsync() {
        Instant start = Instant.now();
        try {
            simulateExpensiveWorkload();
        } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Async operation was interrupted", interruptedException);
        }
        long duration = Duration.between(start, Instant.now()).toMillis();
        AsyncOperationResult result = new AsyncOperationResult("Async operation completed", duration);
        logger.info("Async operation completed in {} ms", duration);
        return CompletableFuture.completedFuture(result);
    }

    private void simulateExpensiveWorkload() throws InterruptedException {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        // Realizar cálculos pesados para consumir CPU con un coste variable
        int complexity = random.nextInt(MIN_WORK_COMPLEXITY, MAX_WORK_COMPLEXITY);
        double accumulator = 0;
        for (int i = 0; i < complexity; i++) {
            accumulator += Math.sqrt(i + 1);
        }
        // Variar también el tiempo de espera para que la duración total cambie en cada ejecución
        long sleepDuration = random.nextLong(MIN_SIMULATED_WORK_DURATION_MS, MAX_SIMULATED_WORK_DURATION_MS + 1);
        Thread.sleep(sleepDuration);
        logger.debug("Simulated workload completed with complexity {} and accumulator {}", complexity, accumulator);
    }
}
