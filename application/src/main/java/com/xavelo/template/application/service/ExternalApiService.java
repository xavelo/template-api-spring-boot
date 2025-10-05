package com.xavelo.template.application.service;

import com.xavelo.template.application.domain.ExternalApiResult;
import com.xavelo.template.application.port.in.CallExternalApiUseCase;
import com.xavelo.template.application.port.out.CallExternalApiPort;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiService implements CallExternalApiUseCase {

    private final CallExternalApiPort callExternalApiPort;

    public ExternalApiService(CallExternalApiPort callExternalApiPort) {
        this.callExternalApiPort = callExternalApiPort;
    }

    @Override
    public ExternalApiResult callExternalApi() {
        return callExternalApiPort.callExternalApi();
    }
}
