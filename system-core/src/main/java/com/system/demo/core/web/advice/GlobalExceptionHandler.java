package com.system.demo.core.web.advice;

import com.system.demo.core.web.exception.BizException;
import com.system.demo.core.web.response.ApiResponse;
import com.system.demo.core.web.response.ResultCode;
import com.system.demo.core.web.response.Results;
import javax.validation.ConstraintViolationException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ApiResponse<Void> handleBizException(BizException exception) {
        return Results.fail(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        BindException.class,
        ConstraintViolationException.class,
        HttpMessageNotReadableException.class,
        HttpMessageConversionException.class
    })
    public ApiResponse<Void> handleValidationException(Exception exception) {
        return Results.fail(ResultCode.BAD_REQUEST.code(), resolveValidationMessage(exception));
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleSystemException(Exception exception) {
        return Results.fail(ResultCode.SYSTEM_ERROR.code(), exception.getMessage());
    }

    private String resolveValidationMessage(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) exception).getBindingResult();
            if (bindingResult.getFieldError() != null) {
                return bindingResult.getFieldError().getDefaultMessage();
            }
        }
        if (exception instanceof BindException) {
            BindingResult bindingResult = ((BindException) exception).getBindingResult();
            if (bindingResult.getFieldError() != null) {
                return bindingResult.getFieldError().getDefaultMessage();
            }
        }
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException violationException = (ConstraintViolationException) exception;
            if (!violationException.getConstraintViolations().isEmpty()) {
                return violationException.getConstraintViolations().iterator().next().getMessage();
            }
        }
        return exception.getMessage();
    }
}
