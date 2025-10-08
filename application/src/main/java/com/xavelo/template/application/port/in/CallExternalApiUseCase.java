package com.xavelo.template.application.port.in;

import com.xavelo.template.application.domain.ExternalApiResult;

public interface CallExternalApiUseCase {

    ExternalApiResult callExternalApi(int status);
}
