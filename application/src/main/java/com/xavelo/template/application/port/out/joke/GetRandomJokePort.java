package com.xavelo.template.application.port.out.joke;

import com.xavelo.template.application.domain.joke.Joke;

public interface GetRandomJokePort {

    Joke getRandomJoke();
}
