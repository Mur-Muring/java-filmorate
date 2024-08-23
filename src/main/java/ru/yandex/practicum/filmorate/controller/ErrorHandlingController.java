package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandlingController {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ArrayList<String> handleConstraintViolationException(ConstraintViolationException ex) {
        ArrayList<String> exceptions = new ArrayList<>();
        exceptions.add(ex.getMessage());
        return exceptions;
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ArrayList<String> handleNotFound(ValidationException ex) {
        ArrayList<String> exceptions = new ArrayList<>();
        exceptions.add(ex.getMessage());
        return exceptions;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ArrayList<String> handleNotReadable(HttpMessageNotReadableException ex) {
        ArrayList<String> exceptions = new ArrayList<>();
        exceptions.add(ex.getMessage());
        return exceptions;
    }
}