package com.fullmugu.nanumeal.exception.handler;

import com.fullmugu.nanumeal.exception.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCUserNotFoundException(CUserNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        log.error("Unexpected Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(CAuthorizationCodeInvalidException.class)
    public ResponseEntity<ErrorResponse> CAuthorizationCodeInvalidException(CAuthorizationCodeInvalidException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        log.error("Unexpected Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(CDuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> CDuplicateEmailException(CDuplicateEmailException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        log.error("Unexpected Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(CDuplicateLoginIdException.class)
    public ResponseEntity<ErrorResponse> CDuplicateLoginIdException(CDuplicateLoginIdException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        log.error("Unexpected Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(CDuplicateNicknameException.class)
    public ResponseEntity<ErrorResponse> CDuplicateNicknameException(CDuplicateNicknameException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        log.error("Unexpected Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(CInvalidHistoryMoneyException.class)
    public ResponseEntity<ErrorResponse> CInvalidHistoryMoneyException(CInvalidHistoryMoneyException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        log.error("Unexpected Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(CRestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponse> CRestaurantNotFoundException(CRestaurantNotFoundException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        log.error("Unexpected Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }

    @ExceptionHandler(CInvalidJwtException.class)
    public ResponseEntity<ErrorResponse> CInvalidJwtException(CInvalidJwtException ex) {
        ErrorResponse response = new ErrorResponse(ex.getErrorCode());
        response.setMessage(ex.getMessage());
        log.error("Unexpected Exception occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
    }
}
