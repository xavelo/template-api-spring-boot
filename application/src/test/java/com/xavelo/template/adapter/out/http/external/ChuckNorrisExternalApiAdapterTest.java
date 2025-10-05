package com.xavelo.template.adapter.out.http.external;

import com.xavelo.template.application.domain.ExternalApiResult;
import com.xavelo.template.configuration.ChuckNorrisProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(ChuckNorrisExternalApiAdapter.class)
@TestPropertySource(properties = "chucknorris.base-url=https://api.chucknorris.io")
@Import(ChuckNorrisProperties.class)
class ChuckNorrisExternalApiAdapterTest {

    @Autowired
    private ChuckNorrisExternalApiAdapter externalApiAdapter;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    void callExternalApiReturnsResult() {
        mockServer.expect(MockRestRequestMatchers.requestTo("/jokes/random"))
                .andRespond(MockRestResponseCreators.withSuccess("""
                        {"id": "test-id",
                         "value": "A hilarious fact",
                         "url": "https://api.chucknorris.io/jokes/test-id"}
                        """, org.springframework.http.MediaType.APPLICATION_JSON));

        ExternalApiResult result = externalApiAdapter.callExternalApi();

        assertThat(result.id()).isEqualTo("test-id");
        assertThat(result.value()).isEqualTo("A hilarious fact");
        assertThat(result.url()).isEqualTo("https://api.chucknorris.io/jokes/test-id");
    }
}
