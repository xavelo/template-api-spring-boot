package com.xavelo.template.adapter.in.http.joke;

import com.xavelo.template.application.domain.joke.Joke;
import com.xavelo.template.application.port.in.joke.GetRandomJokeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jokes")
public class JokeController {

    private final GetRandomJokeUseCase getRandomJokeUseCase;

    public JokeController(GetRandomJokeUseCase getRandomJokeUseCase) {
        this.getRandomJokeUseCase = getRandomJokeUseCase;
    }

    @GetMapping("/random")
    public ResponseEntity<Joke> randomJoke() {
        Joke joke = getRandomJokeUseCase.getRandomJoke();
        return ResponseEntity.ok(joke);
    }
}
