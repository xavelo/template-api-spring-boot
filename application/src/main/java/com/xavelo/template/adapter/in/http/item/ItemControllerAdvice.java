package com.xavelo.template.adapter.in.http.item;

import com.xavelo.template.api.contract.model.ErrorResponseDto;
import com.xavelo.template.application.exception.ItemNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = ItemController.class)
public class ItemControllerAdvice {

    private static final Logger logger = LogManager.getLogger(ItemControllerAdvice.class);

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(ItemNotFoundException exception) {
        logger.warn("Item not found: {}", exception.getMessage());
        ErrorResponseDto error = new ErrorResponseDto().message(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
