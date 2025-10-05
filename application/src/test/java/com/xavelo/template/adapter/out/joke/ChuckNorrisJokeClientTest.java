package com.xavelo.template.adapter.out.joke;

import com.xavelo.template.configuration.ChuckNorrisProperties;
import com.xavelo.template.joke.Joke;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(ChuckNorrisJokeClient.class)
@TestPropertySource(properties = "chucknorris.base-url=https://api.chucknorris.io")
@Import(ChuckNorrisProperties.class)
class ChuckNorrisJokeClientTest {

    @Autowired
    private ChuckNorrisJokeClient jokeClient;

    @Autowired
    private MockRestServiceServer mockServer;

    @Test
    void fetchRandomJokeReturnsJoke() {
        mockServer.expect(MockRestRequestMatchers.requestTo("/jokes/random"))
                .andRespond(MockRestResponseCreators.withSuccess("""
                        {
                          \"id\": \"test-id\",
                          \"value\": \"A hilarious joke\",
                          \"url\": \"https://api.chucknorris.io/jokes/test-id\"
                        }
                        """, org.springframework.http.MediaType.APPLICATION_JSON));

        Joke joke = jokeClient.fetchRandomJoke();

        assertThat(joke.id()).isEqualTo("test-id");
        assertThat(joke.value()).isEqualTo("A hilarious joke");
        assertThat(joke.url()).isEqualTo("https://api.chucknorris.io/jokes/test-id");
    }
}
