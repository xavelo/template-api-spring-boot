package com.xavelo.template.api.application.service;

import java.util.logging.Logger;

import com.xavelo.template.port.in.AsynchExpensiveOperationUseCase;
import com.xavelo.template.port.in.SynchExpensiveOperationUseCase;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class ExpensiveOperationService implements AsynchExpensiveOperationUseCase, SynchExpensiveOperationUseCase {

    private static final Logger logger = Logger.getLogger(ExpensiveOperationService.class.getName());
    
    private static final int LOOP = 10_000_000;

    @Override
    public Mono<Long> nonBlockingExpensiveOperation() {
        return Mono.fromCallable(() -> {
            long startTime = System.currentTimeMillis();
            long value = expensiveOperation();
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Asynch expensiveOperation executed LOOP " + LOOP + " in " + duration + " ms - result: " + value);
            return value;
        });
    }

    @Override
    public Long blockingExpensiveOperation() {
        long startTime = System.currentTimeMillis();
        long value = expensiveOperation();
        long duration = System.currentTimeMillis() - startTime;
        logger.info("Blocking expensiveOperation executed LOOP " + LOOP + " in " + duration + " ms - result: " + value);
        return value;
    }

    private long expensiveOperation() {
        long sum = 0;
        // Simulate latency with CPU-intensive calculations
        for (int i = 0; i < LOOP; i++) {
            sum += Math.sqrt(i + (int)(Math.random() * 1000));
            sum += Math.pow(i, 2); // Additional CPU-intensive operation
            sum += fibonacci(i % 10); // Another CPU-intensive operation
        }
        return sum;
    }

    // Helper method for calculating Fibonacci numbers
    private int fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

}
