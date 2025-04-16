package com.xavelo.template.api.application.service;

import java.util.logging.Logger; // Add this import

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class LatencyService {

    private static final Logger logger = Logger.getLogger(LatencyService.class.getName());
    
    private static final int LOOP = 1_000_000;

    public Mono<Long> getLatencyAsynch() {
        return Mono.fromCallable(() -> {
            long startTime = System.currentTimeMillis(); // Start time
            long sum = 0; // Initialize sum variable
            // Simulate latency with CPU-intensive calculations
            for (int i = 0; i < LOOP; i++) {
                sum += Math.sqrt(i + (int)(Math.random() * 1000));
                sum += Math.pow(i, 2); // Additional CPU-intensive operation
                sum += fibonacci(i % 10); // Another CPU-intensive operation
            }
            long duration = System.currentTimeMillis() - startTime; // Calculate duration
            logger.info("getLatencyAsynch executed LOOP " + LOOP + " in " + duration + " ms - result: " + sum); // Log duration
            return sum; // Return the sum
        }); // Ensure it returns Mono<Integer>
    }

    public Long getLatency() {
        long startTime = System.currentTimeMillis(); // Start time
        // Simulate latency with CPU-intensive calculations
        long sum = 0; // Initialize sum variable
            // Simulate latency with CPU-intensive calculations
            for (int i = 0; i < LOOP; i++) {
                sum += Math.sqrt(i + (int)(Math.random() * 1000));
                sum += Math.pow(i, 2); // Additional CPU-intensive operation
                sum += fibonacci(i % 10); // Another CPU-intensive operationAdd to sum
            }
        long duration = System.currentTimeMillis() - startTime; // Calculate duration
        logger.info("getLatency executed LOOP " + LOOP + " in " + duration + " ms - result: " + sum); // Log duration
        return sum;
    }

    // Helper method for calculating Fibonacci numbers
    private int fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

}
