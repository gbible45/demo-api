package com.demo.api.framework.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

//@ControllerAdvice
@Slf4j
public class XpertControllerAdvice {

    @RequestMapping(produces = "application/json")
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody VndErrors itemNotFoundExceptionHandler(ItemNotFoundException ex) {
        return new VndErrors("error", ex.getMessage());
    }

    @RequestMapping(produces = "application/json")
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody VndErrors handleConstraintViolationException(ConstraintViolationException ex) {
        return new VndErrors("error", ex.getMessage());
    }
}
