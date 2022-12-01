package com.kob.backend.config;

import com.kob.backend.exception.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @Autowired
    private ResponseCodeConfig responseCodeConfig;

    /**
     * 处理Http异常
     */
    @ExceptionHandler(HttpException.class)
    @ResponseBody
    public ResponseEntity<UnifyResponse> handleHttpException(HttpServletRequest req, HttpException e) {
        //动态设置HttpStatus
        HttpStatus httpStatus = HttpStatus.resolve(e.getHttpStatusCode());
        String message = responseCodeConfig.getMessage(e.getCode());
        String RequestURL = req.getMethod() + " " + req.getRequestURI();
        UnifyResponse resp = new UnifyResponse(e.getCode(), message, RequestURL);
        return new ResponseEntity<UnifyResponse>(resp, httpStatus);
    }

    /**
     * 处理未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public UnifyResponse handleException(HttpServletRequest req, Exception e) {
        int code = 1;
        e.printStackTrace();
        String RequestURL = req.getMethod() + " " + req.getRequestURI();
        return new UnifyResponse(code, e.getMessage(), RequestURL);
    }


}