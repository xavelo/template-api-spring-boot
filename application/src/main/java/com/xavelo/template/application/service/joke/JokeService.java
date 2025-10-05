package com.xavelo.template.application.service.joke;

import com.xavelo.template.application.domain.joke.Joke;
import com.xavelo.template.application.port.in.joke.GetRandomJokeUseCase;
import com.xavelo.template.application.port.out.joke.GetRandomJokePort;
import org.springframework.stereotype.Service;

@Service
public class JokeService implements GetRandomJokeUseCase {

    private final GetRandomJokePort getRandomJokePort;

    public JokeService(GetRandomJokePort getRandomJokePort) {
        this.getRandomJokePort = getRandomJokePort;
    }

    @Override
    public Joke getRandomJoke() {
        return getRandomJokePort.getRandomJoke();
    }
}
