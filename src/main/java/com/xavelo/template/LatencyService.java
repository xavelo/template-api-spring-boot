package com.xavelo.template;

import java.util.logging.Logger; // Add this import

public class LatencyService {
    private static final Logger logger = Logger.getLogger(LatencyService.class.getName()); // Initialize logger

    public LatencyService() {
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
