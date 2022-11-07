package com.fullmugu.nanumeal.exception.handler;

import com.fullmugu.nanumeal.exception.CUserNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCUserNotFoundException(CUserNotFoundException ex){
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

}
