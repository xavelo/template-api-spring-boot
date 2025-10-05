package com.xavelo.template.adapter.in.http.joke;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.api.contract.api.JokeApi;
import com.xavelo.template.api.contract.model.JokeDto;
import com.xavelo.template.application.domain.Joke;
import com.xavelo.template.application.port.in.GetRandomJokeUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Adapter
@RestController
@RequestMapping("/api/jokes")
public class JokeController implements JokeApi {

    private static final String DEFAULT_CATEGORY = "general";

    private final GetRandomJokeUseCase getRandomJokeUseCase;

    public JokeController(GetRandomJokeUseCase getRandomJokeUseCase) {
        this.getRandomJokeUseCase = getRandomJokeUseCase;
    }

    @Override
    @GetMapping("/random")
    @CountAdapterInvocation(
            name = "joke-random",
            direction = AdapterMetrics.Direction.IN,
            type = AdapterMetrics.Type.HTTP)
    public ResponseEntity<JokeDto> getRandomJoke(@RequestParam(value = "category", required = false) String category) {
        Joke joke = getRandomJokeUseCase.getRandomJoke();
        JokeDto jokeDto = mapToDto(joke, category);
        return ResponseEntity.ok(jokeDto);
    }

    private JokeDto mapToDto(Joke joke, String requestedCategory) {
        OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC);
        String category = (requestedCategory == null || requestedCategory.isBlank())
                ? DEFAULT_CATEGORY
                : requestedCategory.trim();

        return new JokeDto()
                .id(joke.id())
                .setup(joke.value())
                .punchline(joke.value())
                .category(category)
                .createdAt(timestamp)
                .updatedAt(timestamp);
    }
}
