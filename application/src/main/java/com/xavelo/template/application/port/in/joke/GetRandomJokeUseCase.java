package com.xavelo.template.application.port.in.joke;

import com.xavelo.template.application.domain.Joke;

public interface GetRandomJokeUseCase {

    Joke getRandomJoke();
}
