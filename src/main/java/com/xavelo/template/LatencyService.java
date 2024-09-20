package com.xavelo.template;

public class LatencyService {

    public LatencyService() {
    }

    public void getLatency() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
