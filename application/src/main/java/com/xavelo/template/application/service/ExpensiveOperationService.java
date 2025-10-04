package com.xavelo.template.application.service;

import com.xavelo.template.application.port.in.SynchExpensiveOperationUseCase;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ExpensiveOperationService implements SynchExpensiveOperationUseCase {

    private static final Logger logger = Logger.getLogger(ExpensiveOperationService.class.getName());
    
    private static final int LOOP = 10_000_000;

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
