package com.xavelo.template.adapter.out.http.external;

import com.xavelo.template.application.domain.ExternalApiResult;
import com.xavelo.template.application.exception.ExternalApiUnavailableException;
import com.xavelo.template.configuration.ExternalApiResilienceConfiguration;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.springboot3.circuitbreaker.autoconfigure.CircuitBreakerAutoConfiguration;
import io.github.resilience4j.springboot3.retry.autoconfigure.RetryAutoConfiguration;
import io.github.resilience4j.springboot3.timelimiter.autoconfigure.TimeLimiterAutoConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RestClientTest(HttpExternalApiAdapter.class)
@Import(ExternalApiResilienceConfiguration.class)
@ImportAutoConfiguration({
        AopAutoConfiguration.class,
        CircuitBreakerAutoConfiguration.class,
        RetryAutoConfiguration.class,
        TimeLimiterAutoConfiguration.class
})
class HttpExternalApiAdapterTest {

    @Autowired
    private HttpExternalApiAdapter externalApiAdapter;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @AfterEach
    void tearDown() {
        externalApiAdapter.clearResponseCache();
        circuitBreakerRegistry.circuitBreaker(ExternalApiResilienceConfiguration.EXTERNAL_API_RESILIENCE_ID).reset();
        mockServer.reset();
    }

    @Test
    void callExternalApiReturnsResult() {
        mockServer.expect(MockRestRequestMatchers.requestTo("/200"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("200 OK"));

        ExternalApiResult result = externalApiAdapter.callExternalApi(200);

        assertThat(result.id()).isEqualTo("200");
        assertThat(result.status()).isEqualTo(200);
        assertThat(result.value()).isEqualTo("200 OK");
        assertThat(result.url()).isEqualTo("https://httpstat.us/200");
        mockServer.verify();
    }

    @Test
    void callExternalApiFallsBackToCachedResultWhenApiUnavailable() {
        mockServer.expect(MockRestRequestMatchers.requestTo("/200"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.OK)
                        .contentType(MediaType.TEXT_PLAIN)
                        .body("200 OK"));

        ExternalApiResult cachedResult = externalApiAdapter.callExternalApi(200);
        mockServer.verify();
        mockServer.reset();

        mockServer.expect(ExpectedCount.times(2), MockRestRequestMatchers.requestTo("/200"))
                .andRespond(MockRestResponseCreators.withServerError());

        ExternalApiResult fallbackResult = externalApiAdapter.callExternalApi(200);

        assertThat(fallbackResult).isEqualTo(cachedResult);
        mockServer.verify();
    }

    @Test
    void callExternalApiThrowsDomainExceptionWhenCacheMissing() {
        mockServer.expect(ExpectedCount.times(2), MockRestRequestMatchers.requestTo("/503"))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.SERVICE_UNAVAILABLE));

        assertThatThrownBy(() -> externalApiAdapter.callExternalApi(503))
                .isInstanceOf(ExternalApiUnavailableException.class)
                .hasMessageContaining("503");
        mockServer.verify();
    }
}
