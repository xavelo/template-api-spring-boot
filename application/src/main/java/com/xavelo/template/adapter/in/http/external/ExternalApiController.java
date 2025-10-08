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

    private static final int DEFAULT_STATUS = 200;

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
            @RequestParam(value = "status", required = false) Integer status) {
        int requestedStatus = (status == null) ? DEFAULT_STATUS : status;
        ExternalApiResult result = callExternalApiUseCase.callExternalApi(requestedStatus);
        ExternalApiResponseDto responseDto = mapToDto(result);
        return ResponseEntity.ok(responseDto);
    }

    private ExternalApiResponseDto mapToDto(ExternalApiResult result) {
        OffsetDateTime timestamp = OffsetDateTime.now(ZoneOffset.UTC);
        return new ExternalApiResponseDto()
                .id(result.id())
                .status(result.status())
                .value(result.value())
                .url(URI.create(result.url()))
                .retrievedAt(timestamp);
    }
}
