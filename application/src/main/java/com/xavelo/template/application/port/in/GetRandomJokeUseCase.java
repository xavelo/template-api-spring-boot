package com.xavelo.template.application.port.in;

import com.xavelo.template.application.domain.Joke;

public interface GetRandomJokeUseCase {

    Joke getRandomJoke();
}
