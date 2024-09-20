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
    private final LatencyService latencyService;

    public LatencyController(GitProperties gitProperties, LatencyService latencyService) {
        this.gitProperties = gitProperties;
        this.latencyService = latencyService;
    }

    @GetMapping("/latency-asynch")
    public Mono<ResponseEntity<LatencyResponse>> latencyAsynch() {
        String commitId = gitProperties.getCommitId();
        LocalDateTime dateTime = LocalDateTime.ofInstant(gitProperties.getCommitTime(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String commitTime = dateTime.format(formatter);
        logger.info("latency asynch from pod {} - commitId {} - commitTime {}", commitId, commitTime, podName);
        
        // Refactor getLatency to return a Mono
        return latencyService.getLatencyAsynch()
            .map(latency -> {
                LatencyResponse response = new LatencyResponse(podName, commitId, commitTime);
                return ResponseEntity.ok(response);
            });
    }

    @GetMapping("/latency-synch")
    public ResponseEntity<LatencyResponse> latencySync() {        
        String commitId = gitProperties.getCommitId();
        LocalDateTime dateTime = LocalDateTime.ofInstant(gitProperties.getCommitTime(), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String commitTime = dateTime.format(formatter);
        logger.info("latency synch from pod {} - commitId {} - commitTime {}", commitId, commitTime, podName);
        latencyService.getLatency();
        LatencyResponse response = new LatencyResponse(podName, commitId, commitTime);
        return ResponseEntity.ok(response);
    }

}
