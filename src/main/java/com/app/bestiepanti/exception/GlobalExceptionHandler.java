package com.app.bestiepanti.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> handleValidationExceptions(MethodArgumentNotValidException ex) {
       Map<String, String> errorMap = new HashMap<>();
       ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
       });
       logger.error("Validation errors: {}", errorMap);
       return errorMap;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleBusinessException(UserNotFoundException ex){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error_message", ex.getMessage());
        logger.error("Validation errors: {}", errorMap);
        return errorMap;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public Map<String, String> handleMaxSizeException(MaxUploadSizeExceededException ex){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("image", "Ukuran file melebihi batas maksimum yang diizinkan yaitu 2MB.");
        logger.error("File upload error: {}", errorMap);
        return errorMap;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(ValidationException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("error_message", ex.getMessage());
        logger.error("Validation error: {}", errorMap);
        return errorMap;
    }

}
