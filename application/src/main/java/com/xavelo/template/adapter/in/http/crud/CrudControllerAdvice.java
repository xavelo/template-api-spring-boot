package com.xavelo.template.adapter.in.http.crud;

import com.xavelo.template.api.contract.model.ErrorResponseDto;
import com.xavelo.template.application.exception.CrudObjectNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = CrudController.class)
public class CrudControllerAdvice {

    private static final Logger logger = LogManager.getLogger(CrudControllerAdvice.class);

    @ExceptionHandler(CrudObjectNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(CrudObjectNotFoundException exception) {
        logger.warn("CrudObject not found: {}", exception.getMessage());
        ErrorResponseDto error = new ErrorResponseDto().message(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
