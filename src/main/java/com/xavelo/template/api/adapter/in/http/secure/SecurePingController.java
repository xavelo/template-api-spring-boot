package com.xavelo.template.api.adapter.in.http.secure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SecurePingController {

    private static final Logger logger = LogManager.getLogger(SecurePingController.class);

    @Value("${HOSTNAME:unknown}")
    private String podName;

    @GetMapping("/secure/ping")
    public Mono<ResponseEntity<String>> ping() {
        return Mono.just(ResponseEntity.ok("pong from " + podName));
    }

}

