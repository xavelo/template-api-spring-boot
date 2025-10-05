package com.xavelo.template.adapter.in.http.external;

import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import com.xavelo.template.api.contract.api.ExternalApiApi;
import com.xavelo.template.api.contract.model.ExternalApiResponseDto;
import com.xavelo.template.application.domain.ExternalApiResult;
import com.xavelo.template.application.port.in.CallExternalApiUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Adapter
@RestController
@RequestMapping("/api/external")
public class ExternalApiController implements ExternalApiApi {

    private static final String DEFAULT_CATEGORY = "general";

    private final CallExternalApiUseCase callExternalApiUseCase;

    public ExternalApiController(CallExternalApiUseCase callExternalApiUseCase) {
        this.callExternalApiUseCase = callExternalApiUseCase;
    }

    @Override
    @GetMapping("/random")
    @CountAdapterInvocation(
            name = "external-api-random",
            direction = AdapterMetrics.Direction.IN,
            type = AdapterMetrics.Type.HTTP)
    public ResponseEntity<ExternalApiResponseDto> callExternalApi(
            @RequestParam(value = "category", required = false) String category) {
        ExternalApiResult result = callExternalApiUseCase.callExternalApi();
        ExternalApiResponseDto responseDto = mapToDto(result, category);
        return ResponseEntity.ok(responseDto);
    }

    private ExternalApiResponseDto mapToDto(ExternalApiResult result, String requestedCategory) {
        OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC);
        String category = (requestedCategory == null || requestedCategory.isBlank())
                ? DEFAULT_CATEGORY
                : requestedCategory.trim();

        return new ExternalApiResponseDto()
                .id(result.id())
                .value(result.value())
                .url(URI.create(result.url()))
                .category(category)
                .requestedCategory(category)
                .retrievedAt(timestamp);
    }
}
