package com.xavelo.template.joke;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = JokeController.class)
@AutoConfigureMockMvc(addFilters = false)
class JokeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JokeService jokeService;

    @Test
    void randomJokeReturnsJoke() throws Exception {
        given(jokeService.getRandomJoke())
                .willReturn(new Joke("test-id", "A funny joke", "https://api.chucknorris.io/jokes/test-id"));

        mockMvc.perform(get("/api/jokes/random").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-id"))
                .andExpect(jsonPath("$.value").value("A funny joke"))
                .andExpect(jsonPath("$.url").value("https://api.chucknorris.io/jokes/test-id"));
    }
}
