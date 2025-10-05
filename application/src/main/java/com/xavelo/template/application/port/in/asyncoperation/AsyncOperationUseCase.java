package com.xavelo.template.application.port.in.asyncoperation;

import com.xavelo.template.application.domain.AsyncOperationResult;

import java.util.concurrent.CompletableFuture;

/**
 * Application port exposing the ability to trigger an asynchronous operation.
 */
public interface AsyncOperationUseCase {

    default AsyncOperationResult triggerAsyncOperation() {
        return triggerAsyncOperationAsync().join();
    }

    CompletableFuture<AsyncOperationResult> triggerAsyncOperationAsync();
}
