package com.xavelo.template;

import java.util.logging.Logger; // Add this import

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class LatencyService {

    private static final Logger logger = Logger.getLogger(LatencyService.class.getName());
    
    private static final int LOOP = 1_000_000_000;

    public LatencyService() {
    }

    public Mono<Void> getLatencyAsynch() {
        return Mono.fromRunnable(() -> {
            long startTime = System.currentTimeMillis(); // Start time
            // Simulate latency with CPU-intensive calculations
            for (int i = 0; i < LOOP; i++) {
                Math.sqrt(i + (int)(Math.random() * 1000)); // Add randomness to the calculation
            }
            long duration = System.currentTimeMillis() - startTime; // Calculate duration
            logger.info("getLatencyAsynch executed LOOP " + LOOP + " in " + duration + " ms"); // Log duration
        }).then(); // Ensure it returns Mono<Void>
    }

    public void getLatency() {
        long startTime = System.currentTimeMillis(); // Start time
        // Simulate latency with CPU-intensive calculations
        for (int i = 0; i < LOOP; i++) {
            Math.sqrt(i + (int)(Math.random() * 1000)); // Add randomness to the calculation
        }
        long duration = System.currentTimeMillis() - startTime; // Calculate duration
        logger.info("getLatency executed LOOP " + LOOP + " in " + duration + " ms"); // Log duration
    }
    
}
