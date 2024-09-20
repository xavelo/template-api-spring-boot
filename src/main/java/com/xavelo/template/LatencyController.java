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
public class LatencyController {

    private static final Logger logger = LogManager.getLogger(LatencyController.class);

    @Value("${HOSTNAME:unknown}")
    private String podName;

    private final GitProperties gitProperties;

    public LatencyController(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @GetMapping("/latency-asynch")
    public Mono<ResponseEntity<LatencyResponse>> latencyAsynch() {
        // Simulate latency
        try {
            Thread.sleep(200); // Simulate 200ms latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
        String commitId = gitProperties.getCommitId();
        LocalDateTime dateTime = LocalDateTime.ofInstant(gitProperties.getCommitTime(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String commitTime = dateTime.format(formatter);
        logger.info("latency from pod {} - commitId {} - commitTime {}", commitId, commitTime, podName);
        
        LatencyResponse response = new LatencyResponse(podName, commitId, commitTime);
        return Mono.just(ResponseEntity.ok(response));
    }

    @GetMapping("/latency-sync")
    public ResponseEntity<LatencyResponse> latencySync() {
        // Simulate latency
        try {
            Thread.sleep(200); // Simulate 200ms latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
        String commitId = gitProperties.getCommitId();
        LocalDateTime dateTime = LocalDateTime.ofInstant(gitProperties.getCommitTime(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String commitTime = dateTime.format(formatter);
        logger.info("latency (sync) from pod {} - commitId {} - commitTime {}", commitId, commitTime, podName);
        
        LatencyResponse response = new LatencyResponse(podName, commitId, commitTime);
        return ResponseEntity.ok(response);
    }

}
