package com.xavelo.template.api.adapter.in.http.ping;

import com.xavelo.template.api.api.model.PingResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class PingController {

    private static final Logger logger = LogManager.getLogger(PingController.class);

    @Value("${HOSTNAME:unknown}")
    private String podName;

    @GetMapping(path = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PingResponseDto> getPing() {
        String message = "pong from " + podName;
        logger.info("Responding to /api/ping with message: {}", message);
        PingResponseDto response = new PingResponseDto().message(message);
        return ResponseEntity.ok(response);
    }
}
