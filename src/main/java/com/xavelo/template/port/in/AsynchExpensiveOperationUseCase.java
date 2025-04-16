package com.xavelo.template.port.in;

import reactor.core.publisher.Mono;

public interface AsynchExpensiveOperationUseCase {
    Mono<Long> nonBlockingExpensiveOperation();
}
