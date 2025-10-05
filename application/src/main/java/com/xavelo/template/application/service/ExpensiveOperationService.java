package com.xavelo.template.application.service;

import com.xavelo.template.application.port.in.ExpensiveOperationUseCase;

public class ExpensiveOperationService implements ExpensiveOperationUseCase {
    @Override
    public long runExpensiveOperation() {
        return 0;
    }
}
