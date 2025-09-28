package com.xavelo.template.api.adapter.in.http.secure;

import com.xavelo.api.dto.PingResponse;
import com.xavelo.api.interfaces.SecurePingApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurePingController implements SecurePingApi {

    private static final Logger logger = LogManager.getLogger(SecurePingController.class);

    @Value("${HOSTNAME:unknown}")
    private String podName;

    @Override
    public ResponseEntity<PingResponse> getSecurePing() {
        String message = "pong from " + podName;
        logger.info("Responding to /api/secure/ping with message: {}", message);
        PingResponse response = new PingResponse().message(message);
        return ResponseEntity.ok(response);
    }
}
