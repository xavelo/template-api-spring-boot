package com.xavelo.template.adapter.in.http.secure;

import com.xavelo.template.api.contract.api.SecurePingApi;
import com.xavelo.template.api.contract.model.PingResponseDto;
import com.xavelo.common.metrics.Adapter;
import com.xavelo.common.metrics.AdapterMetrics;
import com.xavelo.common.metrics.CountAdapterInvocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Adapter("")
public class SecurePingController implements SecurePingApi {

    private static final Logger logger = LogManager.getLogger(SecurePingController.class);

    @Value("${HOSTNAME:unknown}")
    private String podName;

    @Override
    @CountAdapterInvocation(
            name = "secure-ping",
            type = AdapterMetrics.Type.HTTP,
            direction = AdapterMetrics.Direction.IN)
    public ResponseEntity<PingResponseDto> getSecurePing() {
        String message = "pong from " + podName;
        logger.info("Responding to /api/secure/ping with message: {}", message);
        PingResponseDto response = new PingResponseDto().message(message);
        return ResponseEntity.ok(response);
    }
}
