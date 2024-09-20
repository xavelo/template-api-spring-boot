package com.xavelo.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.info.GitProperties;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import static org.mockito.Mockito.when;

class PingControllerTest {

    @Mock
    private GitProperties gitProperties;

    private PingController pingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pingController = new PingController(gitProperties);
        ReflectionTestUtils.setField(pingController, "podName", "test-pod");
    }

    @Test
    void testPing() {
        // Arrange
        String commitId = "abc123";
        Instant commitTime = Instant.parse("2023-04-01T12:00:00Z");
        Properties props = new Properties();
        props.setProperty("commit.id", commitId);
        props.setProperty("commit.time", commitTime.toString());

        when(gitProperties.get("commit.id")).thenReturn(commitId);
        when(gitProperties.getCommitId()).thenReturn(commitId);
        when(gitProperties.getCommitTime()).thenReturn(commitTime);

        LocalDateTime dateTime = LocalDateTime.ofInstant(commitTime, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedCommitTime = dateTime.format(formatter);

        // Act
        Mono<String> result = pingController.ping();

        // Assert
        StepVerifier.create(result)
                .expectNext("pong from pod test-pod - " + commitId + " - " + formattedCommitTime)
                .verifyComplete();
    }
}
