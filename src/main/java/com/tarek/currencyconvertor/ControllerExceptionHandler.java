package com.tarek.currencyconvertor;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler({ ConstraintViolationException.class,
            IllegalArgumentException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String badArgsHandler(Exception ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(LatestRatesRetrievalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String latestRatesRetrievalExceptionHandler(
            LatestRatesRetrievalException ex) {
        return ex.getMessage();
    }
}
