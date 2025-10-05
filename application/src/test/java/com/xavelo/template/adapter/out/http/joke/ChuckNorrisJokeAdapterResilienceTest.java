package com.xavelo.template.adapter.out.http.joke;

import com.xavelo.template.application.domain.Joke;
import com.xavelo.template.application.exception.JokeUnavailableException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.client.response.DefaultResponseCreator;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureMockRestServiceServer
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestPropertySource(properties = {
        "chucknorris.base-url=http://localhost",
        "resilience4j.retry.instances.chuck-norris-random.max-attempts=3",
        "resilience4j.retry.instances.chuck-norris-random.wait-duration=5ms",
        "resilience4j.timelimiter.instances.chuck-norris-random.timeout-duration=1s"
})
class ChuckNorrisJokeAdapterResilienceTest {

    @Autowired
    private ChuckNorrisJokeAdapter jokeAdapter;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    void fallbackReturnsCachedJokeWhenFailuresOccur() {
        Joke expectedJoke = new Joke("test-id", "A hilarious joke", "https://api.chucknorris.io/jokes/test-id");

        mockServer.expect(MockRestRequestMatchers.requestTo("/jokes/random"))
                .andRespond(successResponse(expectedJoke));

        Joke firstCallResult = jokeAdapter.getRandomJoke();

        assertThat(firstCallResult).isEqualTo(expectedJoke);

        mockServer.verify();
        mockServer.reset();

        for (int i = 0; i < 3; i++) {
            mockServer.expect(MockRestRequestMatchers.requestTo("/jokes/random"))
                    .andRespond(MockRestResponseCreators.withServerError());
        }

        Joke fallbackResult = jokeAdapter.getRandomJoke();

        assertThat(fallbackResult).isEqualTo(expectedJoke);
        mockServer.verify();
    }

    @Test
    void fallbackThrowsDomainExceptionWhenNoCacheAvailable() {
        for (int i = 0; i < 3; i++) {
            mockServer.expect(MockRestRequestMatchers.requestTo("/jokes/random"))
                    .andRespond(MockRestResponseCreators.withServerError());
        }

        assertThatThrownBy(() -> jokeAdapter.getRandomJoke())
                .isInstanceOf(JokeUnavailableException.class)
                .hasMessageContaining("Chuck Norris jokes are temporarily unavailable");

        mockServer.verify();
    }

    private DefaultResponseCreator successResponse(Joke joke) {
        return MockRestResponseCreators.withSuccess("""
                {
                  \"id\": \"%s\",
                  \"value\": \"%s\",
                  \"url\": \"%s\"
                }
                """.formatted(joke.id(), joke.value(), joke.url()), MediaType.APPLICATION_JSON);
    }
}
