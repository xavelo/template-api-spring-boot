package com.xavelo.template;

import java.util.logging.Logger; // Add this import

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class LatencyService {
    private static final Logger logger = Logger.getLogger(LatencyService.class.getName()); // Initialize logger

    public LatencyService() {
    }

    public Mono<Void> getLatencyAsynch() {
        return Mono.fromRunnable(() -> {
            long startTime = System.currentTimeMillis(); // Start time
            try {
                Thread.sleep(500); // Simulate latency
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
            } finally {
                long duration = System.currentTimeMillis() - startTime; // Calculate duration
                logger.info("getLatency executed in " + duration + " ms"); // Log duration
            }
        }).then(); // Ensure it returns Mono<Void>
    }

    public void getLatency() {
        long startTime = System.currentTimeMillis(); // Start time
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            long duration = System.currentTimeMillis() - startTime; // Calculate duration
            logger.info("getLatency executed in " + duration + " ms"); // Log duration
        }
    }
}
