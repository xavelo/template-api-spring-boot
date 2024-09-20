package com.xavelo.template;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.GitProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RestController
public class PingController {

    private static final Logger logger = LogManager.getLogger(PingController.class);

    @Value("${HOSTNAME:unknown}")
    private String podName;

    private final GitProperties gitProperties;

    public PingController(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @GetMapping("/ping")
    public Mono<ResponseEntity<PingResponse>> ping() {
        String commitId = gitProperties.getCommitId();
        LocalDateTime dateTime = LocalDateTime.ofInstant(gitProperties.getCommitTime(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String commitTime = dateTime.format(formatter);
        logger.info("pong from pod {} - commitId {} - commitTime {}", commitId, commitTime, podName);
        
        PingResponse response = new PingResponse(podName, commitId, commitTime);
        return Mono.just(ResponseEntity.ok(response));
    }

    @GetMapping("/test")
    public Mono<String> test() {
        logger.info("test");
        return Mono.just("test");
    }

}

