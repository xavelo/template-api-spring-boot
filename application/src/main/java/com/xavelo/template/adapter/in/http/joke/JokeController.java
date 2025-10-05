package com.xavelo.template.adapter.in.http.joke;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.application.domain.Joke;
import com.xavelo.template.application.port.in.joke.GetRandomJokeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Adapter
@RestController
@RequestMapping("/api/jokes")
public class JokeController {

    private final GetRandomJokeUseCase getRandomJokeUseCase;

    public JokeController(GetRandomJokeUseCase getRandomJokeUseCase) {
        this.getRandomJokeUseCase = getRandomJokeUseCase;
    }

    @GetMapping("/random")
    @CountAdapterInvocation(
            name = "joke-random",
            direction = AdapterMetrics.Direction.IN,
            type = AdapterMetrics.Type.HTTP)
    public ResponseEntity<Joke> randomJoke() {
        Joke joke = getRandomJokeUseCase.getRandomJoke();
        return ResponseEntity.ok(joke);
    }
}
