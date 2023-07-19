package com.leo.gulimall.product.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.leo.gulimall.product.controller")
public class GulimallExceprionControllerAdvice {

    @ExceptionHandler
    public void handleValidException() {

    }
}
