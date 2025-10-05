package com.xavelo.template.joke;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/jokes")
public class JokeController {

    private final JokeService jokeService;

    public JokeController(JokeService jokeService) {
        this.jokeService = jokeService;
    }

    @GetMapping("/random")
    public ResponseEntity<Joke> randomJoke() {
        Joke joke = jokeService.getRandomJoke();
        return ResponseEntity.ok(joke);
    }
}
