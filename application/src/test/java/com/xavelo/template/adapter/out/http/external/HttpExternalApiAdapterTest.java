package com.xavelo.template.adapter.out.http.external;

import com.xavelo.template.application.domain.ExternalApiResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(HttpExternalApiAdapter.class)
class HttpExternalApiAdapterTest {

    @Autowired
    private HttpExternalApiAdapter externalApiAdapter;

    @Autowired
    private MockRestServiceServer mockServer;

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
    }
}
