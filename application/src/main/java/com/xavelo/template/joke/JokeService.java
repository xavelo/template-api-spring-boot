package com.xavelo.template.joke;

import com.xavelo.template.adapter.out.joke.JokeClient;
import org.springframework.stereotype.Service;

@Service
public class JokeService {

    private final JokeClient jokeClient;

    public JokeService(JokeClient jokeClient) {
        this.jokeClient = jokeClient;
    }

    public Joke getRandomJoke() {
        return jokeClient.fetchRandomJoke();
    }
}
