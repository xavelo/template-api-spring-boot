package com.xavelo.template.adapter.out.http.external;

import com.xavelo.template.application.domain.ExternalApiResult;
import com.xavelo.template.application.exception.ExternalApiUnavailableException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class HttpExternalApiAdapterTest {

    private static final MockWebServer mockWebServer = new MockWebServer();

    static {
        try {
            mockWebServer.start();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start MockWebServer", e);
        }
    }

    @Autowired
    private HttpExternalApiAdapter externalApiAdapter;

    @Autowired
    private ExternalApiResponseCache responseCache;

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @AfterEach
    void reset() {
        responseCache.clear();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("external.api.base-url", () -> mockWebServer.url("/status").toString());
    }

    @Test
    void callExternalApiReturnsResult() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("200 OK"));

        ExternalApiResult result = externalApiAdapter.callExternalApi(200);

        assertThat(result.id()).isEqualTo("200");
        assertThat(result.status()).isEqualTo(200);
        assertThat(result.value()).isEqualTo("200 OK");
        assertThat(result.url()).isEqualTo(mockWebServer.url("/status/200").toString());

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertThat(request).isNotNull();
        assertThat(request.getPath()).isEqualTo("/status/200");
    }

    @Test
    void callExternalApiFallsBackToCachedResultWhenApiUnavailable() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("200 OK"));

        ExternalApiResult cachedResult = externalApiAdapter.callExternalApi(200);
        mockWebServer.takeRequest(1, TimeUnit.SECONDS);

        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        ExternalApiResult fallbackResult = externalApiAdapter.callExternalApi(200);

        assertThat(fallbackResult).isEqualTo(cachedResult);
        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertThat(request).isNotNull();
        assertThat(request.getPath()).isEqualTo("/status/200");
    }

    @Test
    void callExternalApiThrowsDomainExceptionWhenCacheMissing() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(503));

        assertThatThrownBy(() -> externalApiAdapter.callExternalApi(503))
                .isInstanceOf(ExternalApiUnavailableException.class)
                .hasMessageContaining("503");

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertThat(request).isNotNull();
        assertThat(request.getPath()).isEqualTo("/status/503");
    }
}
