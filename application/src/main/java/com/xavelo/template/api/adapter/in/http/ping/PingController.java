package com.xavelo.template.api.adapter.in.http.ping;

import com.xavelo.api.dto.PingResponse;
import com.xavelo.api.interfaces.PingApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController implements PingApi {

    private static final Logger logger = LogManager.getLogger(PingController.class);

    @Value("${HOSTNAME:unknown}")
    private String podName;

    @Override
    public ResponseEntity<PingResponse> getPing() {
        String message = "pong from " + podName;
        logger.info("Responding to /api/ping with message: {}", message);
        PingResponse response = new PingResponse().message(message);
        return ResponseEntity.ok(response);
    }
}
