package com.techaccelarators.ifind.exception;

import com.techaccelarators.ifind.util.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //handles specific errors
    @ExceptionHandler(value = RecordNotFoundException.class)
    public final Response<Object> handleRecordNotFoundException(RecordNotFoundException exception) {
        return new Response<>().buildErrorResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    public final Response<Object> handleInvalidRequestException(InvalidRequestException exception) {
        return new Response<>().buildErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NotImplementedException.class)
    public final Response<Object> handleNotImplementedException(NotImplementedException exception) {
        return new Response<>().buildErrorResponse(exception.getMessage(), HttpStatus.I_AM_A_TEAPOT);
    }

    //handles global errors
    @ExceptionHandler(Exception.class)
    public Response<Object> handleGlobalException(Exception exception) {
        exception.printStackTrace();
        return new Response<>().buildErrorResponse("An Error occurred, Contact the Admin for assistance",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
