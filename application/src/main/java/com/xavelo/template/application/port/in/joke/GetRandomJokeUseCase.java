package com.xavelo.template.application.port.in.joke;

import com.xavelo.template.application.domain.joke.Joke;

public interface GetRandomJokeUseCase {

    Joke getRandomJoke();
}
