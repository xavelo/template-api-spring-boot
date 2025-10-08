package com.xavelo.template.application.port.out;

import com.xavelo.template.application.domain.ExternalApiResult;

public interface CallExternalApiPort {

    ExternalApiResult callExternalApi(int status);
}
