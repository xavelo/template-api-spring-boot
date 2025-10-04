package com.xavelo.template.adapter.in.http.ping;

import com.xavelo.template.api.contract.api.PingApi;
import com.xavelo.template.api.contract.model.PingResponseDto;
import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Adapter
@RestController
public class PingController implements PingApi {

    private static final Logger logger = LogManager.getLogger(PingController.class);

    @Value("${HOSTNAME:unknown}")
    private String podName;


    @CountAdapterInvocation(
            name = "ping",
            direction = AdapterMetrics.Direction.IN,
            type = AdapterMetrics.Type.HTTP)
    @Override
    public ResponseEntity<PingResponseDto> getPing() {
        String message = "pong from " + podName;
        logger.info("Responding to /api/ping with message: {}", message);
        PingResponseDto response = new PingResponseDto().message(message);
        return ResponseEntity.ok(response);
    }
}
